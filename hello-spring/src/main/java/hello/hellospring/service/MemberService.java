package hello.hellospring.service;

import hello.hellospring.DTO.MemberChangeDTO;
import hello.hellospring.DTO.MemberRegistDTO;
import hello.hellospring.DTO.MemberRemoveDTO;
import hello.hellospring.DTO.MemberSearchDTO;
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

    public void removeMember(MemberRemoveDTO memberRemoveDTO) {
        if(memberRepository.existsByEmail(memberRemoveDTO.getEmail())){
            Optional<Member> member = memberRepository.findByEmail(memberRemoveDTO.getEmail());
            if(member.get().getPassword().equals(memberRemoveDTO.getPassword())){
                memberRepository.delete(member.get());
            }
            else throw new InvalidValueException(ErrorCode.WRONG_PASSWORD);//패스워드 불일치
        }else throw new InvalidValueException(ErrorCode.MEMBER_NOT_FOUND);//회원 없음
    }

    public boolean searchMember(MemberSearchDTO memberSearchDTO){
        Optional<Member> result = memberRepository.findByEmail(memberSearchDTO.getEmail());
        if(result.isPresent())
            return true;
        else
            return false;
    }

    public void changeMember(MemberChangeDTO memberChangeDTO) {
        Optional<Member> result = memberRepository.findByEmail(memberChangeDTO.getEmail());
        if(result.isPresent()){
            if(result.get().getPassword().equals(memberChangeDTO.getPassword())){
                Member newMember = result.get();
                newMember.setPassword(memberChangeDTO.getNew_password());
                memberRepository.save(newMember);
            }
            else throw new InvalidValueException(ErrorCode.WRONG_PASSWORD);//패스워드 불일치

        }
        else throw new InvalidValueException(ErrorCode.MEMBER_NOT_FOUND);//회원 없음
    }
}
