package login.securitylogin.config;

import login.securitylogin.config.jwt.TokenAuthenticationFilter;
import login.securitylogin.config.jwt.TokenProvider;
import login.securitylogin.config.jwt.repository.RefreshTokenRepository;
import login.securitylogin.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import login.securitylogin.config.oauth.OAuth2SuccessHandler;
import login.securitylogin.config.oauth.OAuth2UserCustomService;
import login.securitylogin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebOAuthSecurityConfig {

    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;



    // Spring Security 제외 목록 (인증,인가 검사 제외)
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/img/**", "/css/**", "/js/**"); // 해당 URL 보안검사 무시.
    }


    // Spring Security 필터 체인 목록
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // csrf, http로그인, form로그인, 로그아웃 비활성화. (토큰방식으로 인증)
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable();

        http.sessionManagement() // 세션관리 설정 (Stateless로 설정 -> 세션 사용X)
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //헤더 확인 커스텀 필터 추가.  (JWT 토큰 필터)(tokenAuthenticationFilter)
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);


        http.authorizeRequests() // 'api/token'은 인증없이 접근 가능 , 나머지 '/api/**'는 인증 필요
                .requestMatchers("/api/token").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll();

        http.oauth2Login() // ouath2 로그인 설정
                .loginPage("/login") // 로그인 진행 페이지
                .authorizationEndpoint()
                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository()) //인증 요청 저장소 설정
                .and()
                .successHandler(oAuth2SuccessHandler()) // 로그인 성공시 실행 핸들러 설정
                .userInfoEndpoint()
                .userService(oAuth2UserCustomService); // OAuth2 적용 서비스 설정

        http.logout() // 로그아웃시 리다이렉트 URL 설정
                .logoutSuccessUrl("/login");


        http.exceptionHandling() // Oauth2 예외처리 설정
                .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        new AntPathRequestMatcher("/api/**")); // 'api'로 시작하는 url인 경우, 401 Unauthorized 응답


        return http.build(); // httpSecurity 객체 빌드후 반환
    }


    @Bean// OAuth2 로그인 성공시 실행 핸들러 -> OAuth2 성공 후 바로 실행.
    public OAuth2SuccessHandler oAuth2SuccessHandler() {

        // OAuth2SuccessHandler를 실행함.
        return new OAuth2SuccessHandler(tokenProvider,
                refreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository(),
                userService
        );
    }

    @Bean // 토큰 인증 필터
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean // 쿠키 기반 OAuth2 인증 요청 저장소를 빈으로 등록
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
