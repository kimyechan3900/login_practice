package login.securitylogin.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Base64;

public class CookieUtil {

    // 쿠키 추가 메서드
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) { // 사용자에게 http 요청을 받음.
        Cookie cookie = new Cookie(name, value); // 쿠키 객체 생성
        cookie.setPath("/");
        cookie.setMaxAge(maxAge); // 쿠키 유효시간 설정

        response.addCookie(cookie); // 응답에 쿠키 추가 -> 클아이언트에게 쿠키 반환
    }

    //쿠키 삭제 메서드
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies(); // 모든 쿠키를 가져옴

        if (cookies == null) {
            return; // 쿠키 없을 시 종료
        }

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) { // 삭제하려는 쿠키 이름 존재 확인
                cookie.setValue(""); // 쿠키 값 제거
                cookie.setPath("/");
                cookie.setMaxAge(0); // 쿠키 유효시간 0 설정
                response.addCookie(cookie); // 응답에 쿠키 추가
            }
        }
    }

    // 객체 직렬화 -> 문자열 반환  메서드
    public static String serialize(Object obj) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(obj));
    }


    // 쿠키 값 디코딩 -> 원래 객체로 반환
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())
                )
        );
    }
}

