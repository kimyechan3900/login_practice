//package login.securitylogin.config.oauth;
//
//import login.securitylogin.domain.User;
//import login.securitylogin.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
//@RequiredArgsConstructor
//@Service
//public class OAuth2UserCustomService extends DefaultOAuth2UserService {
//
//    private final UserRepository userRepository;
//
//
//    //리소스 서버 -> 스프링 서버 (사용자 정보 전달)
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2User user = super.loadUser(userRequest); // ❶ 요청을 바탕으로 유저 정보를 담은 객체 반환
//        saveOrUpdate(user); // 메서드 호출
//
//        return user;
//    }
//
//    // ❷ 유저가 있으면 업데이트, 없으면 유저 생성
//    private User saveOrUpdate(OAuth2User oAuth2User) {
//        Map<String, Object> attributes = oAuth2User.getAttributes(); // OAuth2User에서 속성 정보 가지고 옴
//
//        String email = (String) attributes.get("email"); // 이메일 정보
//        String name = (String) attributes.get("name"); // 이름 정보
//
//
//        User user = userRepository.findByEmail(email) // 이메일로 사용자 찾음 (있으면 사용자 이름 업데이트, 없으면 새로 생성)
//                .map(entity -> entity.update(name))
//                .orElse(User.builder()
//                        .email(email)
//                        .nickname(name)
//                        .build());
//
//        return userRepository.save(user); // 레포에 저장 및 업데이트 후 반환
//    }
//}
//
