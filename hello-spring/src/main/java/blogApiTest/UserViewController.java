package blogApiTest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

    @GetMapping("/login") // 로그인창 View 메서드
    public String login() {
        return "login";
    }

    @GetMapping("/signup") // 회원가입 View 메서드
    public String signup() {
        return "signup";
    }

}
