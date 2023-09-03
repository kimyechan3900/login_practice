package hello.hellospring.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MemberRemoveDTO {
    private String email;
    private String password;
}
