package hello.hellospring.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberChangeDTO {
    private String email;
    private String password;
    private String new_password;

}
