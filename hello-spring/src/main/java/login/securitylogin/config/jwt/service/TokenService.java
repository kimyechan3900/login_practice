package login.securitylogin.config.jwt.service;

import login.securitylogin.config.jwt.TokenProvider;
import login.securitylogin.domain.User;
import login.securitylogin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    //refreshToken을 이용해 accessToken을 생성
    public String createNewAccessToken(String refreshToken) {

        // 토큰 유효성 검사에 실패하면 예외 발생
        // tokenProvider를 이용해 refresh 토큰의 유효성 검사진행
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        //refresh 토큰 -> 사용자의 ID값 -> 사용자 엔티티
        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);

        //tokenProvider를 이용해 유효시간(2시간)을 가진 access 토큰 발행.
        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}

