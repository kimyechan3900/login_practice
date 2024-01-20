package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

    @GetMapping("/")
    public String control_home(){
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping(value="/remove")
    public String removeForm(){
        return "removeMember";
    }

    @GetMapping(value="/change")
    public String change(){
        return "changeUser";
    }
}