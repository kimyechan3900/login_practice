package hello.hellospring.controller;

import hello.hellospring.Model.User;
import hello.hellospring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/members")
public class ListController {
    private UserService userService;

    @Autowired
    public ListController(UserService userService) {
        this.userService = userService;
    }


}
