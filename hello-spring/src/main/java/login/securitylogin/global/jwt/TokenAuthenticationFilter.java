package login.securitylogin.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor // 토큰 필터 클래스
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization"; //HTTP 요청 헤더에서 Authorization 정보를 가지고 옴.
    private final static String TOKEN_PREFIX = "Bearer "; // 토큰 접두사 지정.


    //필터 메서드 (HTTP 요청이 올 때마다 실행됨) (스프링에서 실행되는게 아닌, Web Context에서 필터링하는 것.)
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, // 요청
            HttpServletResponse response, // 응답
            FilterChain filterChain)  throws ServletException, IOException { // 필터체인

        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        String token = getAccessToken(authorizationHeader); // 헤더에서 토큰 정보를 가지고 옴

        System.out.println("doFilterInternal");

        //토큰 유효성 검사(OAuth2에선 사용 X)
        if (tokenProvider.validToken(token)) {
            //토큰 정보를 활용해 Authentication 객체 가져옴
            Authentication authentication = tokenProvider.getAuthentication(token);
            //Security Context에 인증 객체를 저장. (컨텍스트 홀더에 저장) -> 컨텍스트 홀더는 Spring Security 내부에 존재함
            //Security Context에 인증 객체를 저장함으로써, Spring에서 해당 정보를 가지고 인증 여부를 알 수 있음.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        //필터 체인 계속 실행
        filterChain.doFilter(request, response);
    }

    //Authorization 헤더에서 토큰 정보를 가져오는 메서드.
    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length()); // 토큰 접두사 제외함
        }

        return null;
    }
}

