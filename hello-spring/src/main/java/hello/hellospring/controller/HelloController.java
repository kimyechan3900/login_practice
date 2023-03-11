package hello.hellospring.controller;

import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Controller
public class HelloController {
    private MemberService memberService;

    @Autowired
    public HelloController(MemberService memberService) {
        this.memberService = memberService;
    }



    @GetMapping("hello")
    public String control(Model model){
        model.addAttribute("data1","data 1");
        model.addAttribute("data2","data 2");
        return "hello";
    }

    @GetMapping("home")
    public String control_home(){
        return "home";
    }

    @GetMapping(value="/members/new")
    public String createForm(){
        return "API/createMemberForm";
    }

    @PostMapping(value="/members/new")
    public String create(MemberForm form)  {//포스트매핑시 넘어온값의 name을 봄 그러면이제 매개변수인  MemberForm으로 들어감
        System.out.println(form.getName());
        Member member=new Member();
        member.setName(form.getName());

        try {
            memberService.join(member);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/home";
    }

    @GetMapping(value="/members/change")
    public String changeForm(){
        return "API/changeMemberForm";
    }

    @PostMapping(value="/members/change")
    public String change(MemberForm form1) {//포스트매핑시 넘어온값의 name을 봄 그러면이제 매개변수인  MemberForm으로 들어감
        String[] name=form1.getName().split(",");
        System.out.println(name[0]+name[1]);
        Member member1=new Member();
        Member member2=new Member();

        member1.setName(name[0]);
        member2.setName(name[1]);

        try {
            memberService.changeMember(member1,member2);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/home";
    }

    @GetMapping("members")
    public String control_listmembers(Model model) {
        List<Member> members= null;
        try {
            members = memberService.findMembers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("members",members);
        return "API/memberList";
    }

    @GetMapping(value="/members/remove")
    public String removeForm(){
        return "API/removeMemberForm";
    }

    @PostMapping(value="/members/remove")
    public String remove(MemberForm form) {//포스트매핑시 넘어온값의 name을 봄 그러면이제 매개변수인  MemberForm으로 들어감
        Member member=new Member();
        member.setName(form.getName());

        try {
            memberService.removeMember(member);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/home";
    }

    @GetMapping(value="/members/search")
    public String searchForm(){
        return "API/searchMemberForm";
    }

    @PostMapping(value="/members/search")
    public String search(MemberForm form) {//포스트매핑시 넘어온값의 name을 봄 그러면이제 매개변수인  MemberForm으로 들어감
        Member member=new Member();
        member.setName(form.getName());

        try {
            if(memberService.searchMember(member)==true)
                return "result/true";
            else
                return "result/False";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value="/members/db")
    public String dbForm(){
        return "index2";
    }

    @PostMapping(value="/members/db")
    public String db() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn= DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/yechan","root","1460"
        );
        PreparedStatement ps =conn.prepareStatement(
                "INSERT INTO table_demo2(seq,name) values(?, ?)"
        );
        ps.setString(1,"9");
        ps.setString(2,"yechan9");
        ps.executeUpdate();
        ps.close();
        conn.close();
        return "redirect:/members/db";
    }
}
