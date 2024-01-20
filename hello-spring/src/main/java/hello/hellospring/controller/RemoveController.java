package hello.hellospring.controller;

import hello.hellospring.DTO.MemberRemoveDTO;
import hello.hellospring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
public class RemoveController {
    private UserService userService;

    @Autowired
    public RemoveController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(value="/remove")
    public String removeForm(){
        return "API/RemoveMember";
    }

    @PostMapping(value="/remove")
    public String remove(MemberRemoveDTO memberRemoveDTO) {//포스트매핑시 넘어온값의 name을 봄 그러면이제 매개변수인  MemberForm으로 들어감

        userService.removeMember(memberRemoveDTO);

        return "redirect:/";
    }
}
