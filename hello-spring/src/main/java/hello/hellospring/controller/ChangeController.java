package hello.hellospring.controller;

import hello.hellospring.DTO.MemberChangeDTO;
import hello.hellospring.DTO.MemberSearchDTO;
import hello.hellospring.Model.Member;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLException;

@Controller
@RequestMapping("/members")
public class ChangeController {
    private MemberService memberService;

    @Autowired
    public ChangeController(MemberService memberService) {
        this.memberService = memberService;
    }


    @GetMapping(value="/change")
    public String change(){
        return "API/ChangeMember";
    }

    @PostMapping(value="/change")
    public String change(MemberChangeDTO memberChangeDTO) {//포스트매핑시 넘어온값의 name을 봄 그러면이제 매개변수인  MemberForm으로 들어감

        memberService.changeMember(memberChangeDTO);

        return "redirect:/";
    }

}
