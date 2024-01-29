package login.securitylogin.global.ouath;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import login.securitylogin.global.util.CookieUtil;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.util.WebUtils;

// OAuth2 인증 요청 -> 쿠키 저장소에 저장.
public class OAuth2AuthorizationRequestBasedOnCookieRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public final static String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request"; //OAuth2 인증 요청을 저장할 쿠키 이름.
    private final static int COOKIE_EXPIRE_SECONDS = 18000; // 쿠키 만료시간

    // 로그인 요청정보를 제거하는 메서드 (이후에 loadAuthorizationRequest 실행)
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("removeAuthorizationRequest");
        return this.loadAuthorizationRequest(request);
    }

    // 로그인 요청정보를 불러오는 메서드 (사용자가 Google Login하고 돌아올 때 발생)
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        System.out.println("loadAuthorizationReqeust");
        Cookie cookie = WebUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME); // request에서 쿠키를 가져옴
        return CookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class); // 쿠키를 역직렬화하여 OAuth2 인증 요청 객체로 변환.
    }

    // 로그인 요청정보를 저장하는 메서드 (사용자가 Google Login 버튼 누를때 발생)
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("saveAuthorizationRequest");
        if (authorizationRequest == null) { // 인증 요청이 NULL인 경우. (중복 로그인, 인증 오류 발생 시)
            removeAuthorizationRequestCookies(request, response); // 쿠키 제거 (이전의 불필요한 쿠키 삭제)
            return;
        }

        // RESPONSE값에 쿠키 추가.
        CookieUtil.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtil.serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS);
    }

    // 로그인 요청정보 쿠키를 제거하는 메서드.
    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("removeAuthorizationRequestCookies");
        CookieUtil.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
    }
}
