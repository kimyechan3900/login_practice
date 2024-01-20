package hello.hellospring.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AddUserRequest {
    private String name;
    private String email;
    private String phone;
    private LocalDate birthdate;
    private String gender;
    private String address;
    private String password;
    private String confirmPassword;
}
