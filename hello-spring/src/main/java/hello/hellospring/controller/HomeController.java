package hello.hellospring.controller;

import hello.hellospring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }



    @GetMapping("hello")
    public String control(Model model){
        model.addAttribute("data1","data 1");
        model.addAttribute("data2","data 2");
        return "hello";
    }


}
