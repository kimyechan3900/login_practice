package hello.hellospring.controller;

import hello.hellospring.Model.Member;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/members")
public class ListController {
    private MemberService memberService;

    @Autowired
    public ListController(MemberService memberService) {
        this.memberService = memberService;
    }


    @GetMapping("/list")
    public String control_listmembers(Model model) {
        List<Member> members= null;
        members = memberService.findMembers();
        model.addAttribute("members",members);
        return "API/MemberList";
    }

}
