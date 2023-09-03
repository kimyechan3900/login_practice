package hello.hellospring.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSearchDTO {
    private String email;
    private String password;
}
