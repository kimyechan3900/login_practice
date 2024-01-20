package hello.hellospring.controller;

import hello.hellospring.DTO.MemberSearchDTO;
import hello.hellospring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
public class SearchController {
    private UserService userService;

    @Autowired
    public SearchController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value="/search")
    public String searchForm(){
        return "API/SearchMember";
    }

    @PostMapping(value="/search")
    public String search(MemberSearchDTO memberSearchDTO) {//포스트매핑시 넘어온값의 name을 봄 그러면이제 매개변수인  MemberForm으로 들어감

        if(userService.searchMember(memberSearchDTO))
            return "result/true";
        else
            return "result/False";
    }


}
