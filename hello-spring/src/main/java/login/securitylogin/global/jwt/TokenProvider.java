package login.securitylogin.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import login.securitylogin.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties; // jwt 설정값 클래스


    //토큰 생성 메서드
    public String generateToken(User user, Duration expiredAt) { // User , 만료시간을 입력받음.
        Date now = new Date(); // 현재시간을 가져옴.
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user); // 만료시간 설정후 토큰 생성
    }


    //토큰 제작 메서드
    private String makeToken(Date expiry, User user) {
        Date now = new Date();

        System.out.println("Token make");

        //jwt 빌더
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더
                .setIssuer(jwtProperties.getIssuer()) // 토큰 발행자
                .setIssuedAt(now) // 토큰 발행 시간
                .setExpiration(expiry) // 토큰 만료 시간
                .setSubject(user.getEmail()) //토큰 주제
                .claim("id", user.getId()) // 클레임
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey()) // 서명 알고리즘 및 키 설정
                .compact();
    }

    //토큰 유효성 검사 메서드
    public boolean validToken(String token) {
        try {
            Jwts.parser() // jwt 파서 활용
                    .setSigningKey(jwtProperties.getSecretKey()) // 스프링 서버의 SecretKey 활용
                    .parseClaimsJws(token); // 토큰 복호화(파싱) -> 유효하지 않을 경우 예외 발생.
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    //토큰에서 인증 정보를 가져오는 메서드
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token); // 토큰에서 클레임 가져옴.
        //클레인 = 토큰의 식별자, 권한, 만료시간이 포함.
        Set<SimpleGrantedAuthority> authorities =
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")); // 토큰 권한설정


        // 인증 객체 생성 후 반환
        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject
                (), "", authorities), token, authorities);
    }


    //토큰에서 사용자 ID 가져오는 메서드
    public Long getUserId(String token) {
        Claims claims = getClaims(token); // 토큰에서 클레임 가져옴.
        return claims.get("id", Long.class); // 사용자 ID반환
    }

    //토큰에서 클레임 가져오는 메서드
    private Claims getClaims(String token) {
        return Jwts.parser() // jwt 파서
                .setSigningKey(jwtProperties.getSecretKey()) // 서명키 설정
                .parseClaimsJws(token) // 토큰 파싱
                .getBody(); // 클레임 반환
    }
}
