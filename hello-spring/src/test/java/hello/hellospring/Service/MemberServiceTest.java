package hello.hellospring.Service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRepository;
import hello.hellospring.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MemberServiceTest {
    MemberService memberService;
    MemoryMemberRepository memberRepository;

    @BeforeEach
    public void beforeEach() {
       memberRepository = new MemoryMemberRepository();
       memberService = new MemberService(memberRepository);
    }

    @Test
    public void 회원가입 (){
        Member member=new Member();
        member.setName("member");

        Long saveId= null;
        try {
            saveId = memberService.join(member);
            Member findMember=memberService.findOne(saveId).get();
            Assertions.assertThat(member.getName()).isEqualTo(findMember.getName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
