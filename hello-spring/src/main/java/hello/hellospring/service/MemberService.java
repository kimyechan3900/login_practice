package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository){
        this.memberRepository=memberRepository;
    }

    public Long join(Member member) throws SQLException {
        validateDuplicateMember(member);

        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) throws SQLException {
        Optional<Member> result=memberRepository.findByName(member.getName());
        if(result!=null) {
            result.ifPresent(m -> {
                throw new IllegalStateException("이미 존재하는 회원입니다.");
            });
        }
    }

    public List<Member> findMembers() throws SQLException {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) throws SQLException {
        return memberRepository.findById(memberId);
    }

    public void removeMember(Member member) throws SQLException {
        Optional<Member> result=memberRepository.findByName(member.getName());

        result.ifPresent(m ->{
            memberRepository.removeName(result.get());
        });
    }
    public boolean searchMember(Member member) throws SQLException {
        Optional<Member> result = memberRepository.findByName(member.getName());
        if(result.isPresent()){
            return true;
        }
        else
            return false;

    }

    public void changeMember(Member member1,Member member2) throws SQLException {
        Optional<Member> result = memberRepository.findByName(member1.getName());
        if(result.isPresent()){
            memberRepository.changeName(result.get(),member2.getName());
        }
    }
}
