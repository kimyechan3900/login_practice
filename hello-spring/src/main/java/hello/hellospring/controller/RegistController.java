package hello.hellospring.controller;

import hello.hellospring.DTO.MemberRegistDTO;
import hello.hellospring.service.MemberService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
public class RegistController {
    private MemberService memberService;

    @Autowired
    public RegistController(MemberService memberService) {
        this.memberService = memberService;
    }


    @GetMapping(value="/regist")
    public String createForm(){
        return "API/RegistMember";
    }


    @PostMapping(value="/regist")
    public String create(MemberRegistDTO member)  {//포스트매핑시 넘어온값의 name을 봄 그러면이제 매개변수인  MemberForm으로 들어감
        System.out.println(member.getName());

        memberService.regist(member);
        return "redirect:/";
    }
}
