package hello.hellospring.controller;

import hello.hellospring.DTO.MemberChangeDTO;
import hello.hellospring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
public class ChangeController {
    private UserService userService;

    @Autowired
    public ChangeController(UserService userService) {
        this.userService = userService;
    }



    @PostMapping(value="/change")
    public String change(MemberChangeDTO memberChangeDTO) {//포스트매핑시 넘어온값의 name을 봄 그러면이제 매개변수인  MemberForm으로 들어감

        userService.changeMember(memberChangeDTO);

        return "redirect:/";
    }

}
