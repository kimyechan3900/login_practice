package hello.hellospring.controller;

import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private MemberService memberService;

    @Autowired
    public HomeController(MemberService memberService) {
        this.memberService = memberService;
    }



    @GetMapping("hello")
    public String control(Model model){
        model.addAttribute("data1","data 1");
        model.addAttribute("data2","data 2");
        return "hello";
    }

    @GetMapping("/")
    public String control_home(){
        return "home";
    }

}
