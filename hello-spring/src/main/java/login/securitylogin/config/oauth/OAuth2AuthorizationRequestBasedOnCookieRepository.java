//package login.securitylogin.config.oauth;
//
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import login.securitylogin.util.CookieUtil;
//import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
//import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
//import org.springframework.web.util.WebUtils;
//
//// OAuth2 인증 요청 -> 쿠키 저장소에 저장.
//public class OAuth2AuthorizationRequestBasedOnCookieRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
//
//    public final static String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request"; //OAuth2 인증 요청을 저장할 쿠키 이름.
//    private final static int COOKIE_EXPIRE_SECONDS = 18000; // 쿠키 만료시간
//
//    // 인증 요청을 제거하는 메서드
//    @Override
//    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
//        return this.loadAuthorizationRequest(request); // request에서 인증 요청 로드함.
//    }
//
//    // 인증 요청을 불러오는 메서드
//    @Override
//    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
//        Cookie cookie = WebUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME); // request에서 쿠키를 가져옴
//        return CookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class); // 쿠키를 역직렬화하여 OAuth2 인증 요청 객체로 변환.
//    }
//
//    // 인증 요청을 저장하는 메서드
//    @Override
//    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
//        if (authorizationRequest == null) { // 인증 요청이 NULL인 경우.
//            removeAuthorizationRequestCookies(request, response); // 쿠키 제거
//            return;
//        }
//
//        // RESPONSE값에 쿠키 추가.
//        CookieUtil.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtil.serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS);
//    }
//
//    // 인증 요청 쿠키를 제거하는 메서드.
//    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
//        CookieUtil.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
//    }
//}
