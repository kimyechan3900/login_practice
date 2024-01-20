package login.securitylogin.config;

import login.securitylogin.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private final UserDetailService userService;

    @Bean // Spring Security 제외 목록 (인증,인가 검사 제외)
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console()) // h2-console
                .requestMatchers("/static/**"); // static 내부 파일
    }

    @Bean // Spring Security 필터 체인 목록
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // 인증 및 인가 설정
                .authorizeRequests()
                    .requestMatchers("/login", "/signup", "/user").permitAll() // 자유 접근 목록
                    .anyRequest().authenticated() // permitAll 제외 모든 요청은 인증 필요
                .and()

                // 폼 기반 로그인 설정
                .formLogin()
                    .loginPage("/login") //로그인 페이지
                    .defaultSuccessUrl("/articles") // 로그인 성공시 이동 페이지
                .and()

                // 로그아웃 설정
                .logout()
                    .logoutSuccessUrl("/login") // 로그아웃 완료시 이동 페이지
                    .invalidateHttpSession(true) // 로그아웃시 세션 전체 삭제 여부 (true -> 전체삭제)
                .and()

                .csrf().disable() // CSRF 공격 방지 활성 여부
                .build();
    }

    @Bean // 인증 관리자 메서드
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService) // 사용자 정보 포함 서비스 설정 -> UserDetails를 상속받은 Service 여야함.
                .passwordEncoder(bCryptPasswordEncoder) // 비밀번호 암호화 인코더 종류 설정
                .and()
                .build();
    }

    @Bean // 비밀번호 인코더 빈으로 등록
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
