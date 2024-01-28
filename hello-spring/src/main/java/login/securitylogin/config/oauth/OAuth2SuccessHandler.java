package login.securitylogin.config.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import login.securitylogin.config.jwt.TokenProvider;
import login.securitylogin.config.jwt.domain.RefreshToken;
import login.securitylogin.config.jwt.repository.RefreshTokenRepository;
import login.securitylogin.domain.User;
import login.securitylogin.service.UserService;
import login.securitylogin.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token"; // 리프레시 토큰 저장하는 쿠키 이름
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14); // 리프레시 토큰 유효기간.
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1); // 액세스 토큰 유효기간.
    public static final String REDIRECT_PATH = "/articles"; // 인증 성공 후 리다이렉트 경로.

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final UserService userService;


    //Oauth2 인증 성공 시 호출되는 메서드.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = token.getPrincipal();

        String registrationId = token.getAuthorizedClientRegistrationId(); // 인증 서비스 구분

        String email;
        if (registrationId.equals("google")) {
            email = (String) oAuth2User.getAttributes().get("email");
        } else if (registrationId.equals("kakao")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
            email = (String) kakaoAccount.get("email");
        } else {
            throw new IllegalArgumentException("Not supported provider : " + registrationId);
        }

        User user = userService.findByEmail(email);
        System.out.println(user);


        //리프레시 토큰 생성 및 저장 후 쿠키 추가
        String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION);
        saveRefreshToken(user.getId(), refreshToken);
        addRefreshTokenToCookie(request, response, refreshToken);

        //액세스 토큰 생성 및 URL 리다이렉트
        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
        String targetUrl = getTargetUrl(accessToken);

        //인증 관련 속성 제거
        clearAuthenticationAttributes(request, response);

        //지정 URL로 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    // 리프레시 토큰을 DB에 저장하는 메서드
    private void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken)) // 기존 리프레시 토큰 업데이트 or 없으면 새로 생성
                .orElse(new RefreshToken(userId, newRefreshToken));

        refreshTokenRepository.save(refreshToken); // 리프레시 토큰 저장소에 저장
    }

    //리프레시 토큰을 쿠키에 추가하는 메서드
    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds(); // 쿠키 만료시간 설정

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME); // 기존 리프레시 토큰쿠키 삭제
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge); // 새로운 리프레시 토큰쿠키 추가
    }

    // 인증 관련 속성, 쿠키 제거 메서드
    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response); // 인증 요청 쿠키 제거
    }

    //리다이렉트할 URL 생성 메서드
    private String getTargetUrl(String token) {
        return UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                .queryParam("token", token) // URL 끝에 쿼리 파라미터에 token값 추가
                .build()
                .toUriString(); // URL 문자열로 반환
    }
}
