package hello.hellospring.service;

import hello.hellospring.DTO.MemberRegistDTO;
import hello.hellospring.Error.ErrorCode;
import hello.hellospring.Error.InvalidValueException;
import hello.hellospring.Model.Member;
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

    public void regist(MemberRegistDTO registDTO) throws InvalidValueException {
        validateDuplicateMember(registDTO); // 중복체크

        Member member = Member.builder()
                .name(registDTO.getName())
                .email(registDTO.getEmail())
                .phone(registDTO.getPhone())
                .birthdate(registDTO.getBirthdate())
                .gender(registDTO.getGender())
                .address(registDTO.getAddress())
                .password(registDTO.getPassword())
                .confirmPassword(registDTO.getConfirmPassword())
                .build();

        memberRepository.save(member);
    }

    private void validateDuplicateMember(MemberRegistDTO member) {
        if(memberRepository.existsByEmail(member.getEmail())) {
            throw new InvalidValueException(ErrorCode.DUPLICATED_EMAIL); // 예시: 중복 멤버에 대한 적절한 에러 코드 사용
        }
    }

    public List<Member> findMembers()  {
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
