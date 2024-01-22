package hello.hellospring.service;

import hello.hellospring.DTO.MemberChangeDTO;
import hello.hellospring.DTO.AddUserRequest;
import hello.hellospring.DTO.MemberRemoveDTO;
import hello.hellospring.DTO.MemberSearchDTO;
import hello.hellospring.Error.ErrorCode;
import hello.hellospring.Error.InvalidValueException;
import hello.hellospring.Model.User;
import hello.hellospring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Long save(AddUserRequest dto) throws InvalidValueException {

        return userRepository.save(User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .birthdate(dto.getBirthdate())
                .gender(dto.getGender())
                .address(dto.getAddress())
                .password(dto.getPassword())
                .build()).getId();

    }


    public List<User> findMembers()  {
        return userRepository.findAll();
    }

    public Optional<User> findOne(Long memberId) throws SQLException {
        return userRepository.findById(memberId);
    }

    public void removeMember(MemberRemoveDTO memberRemoveDTO) {
        if(userRepository.existsByEmail(memberRemoveDTO.getEmail())){
            Optional<User> member = userRepository.findByEmail(memberRemoveDTO.getEmail());
            if(member.get().getPassword().equals(memberRemoveDTO.getPassword())){
                userRepository.delete(member.get());
            }
            else throw new InvalidValueException(ErrorCode.WRONG_PASSWORD);//패스워드 불일치
        }else throw new InvalidValueException(ErrorCode.MEMBER_NOT_FOUND);//회원 없음
    }

    public boolean searchMember(MemberSearchDTO memberSearchDTO){
        Optional<User> result = userRepository.findByEmail(memberSearchDTO.getEmail());
        if(result.isPresent())
            return true;
        else
            return false;
    }

    public void changeMember(MemberChangeDTO memberChangeDTO) {
        Optional<User> result = userRepository.findByEmail(memberChangeDTO.getEmail());
        if(result.isPresent()){
            if(result.get().getPassword().equals(memberChangeDTO.getPassword())){
                User newUser = result.get();
                newUser.setPassword(memberChangeDTO.getNew_password());
                userRepository.save(newUser);
            }
            else throw new InvalidValueException(ErrorCode.WRONG_PASSWORD);//패스워드 불일치

        }
        else throw new InvalidValueException(ErrorCode.MEMBER_NOT_FOUND);//회원 없음
    }

}
