package hello.hellospring.Model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "members") // 데이터베이스에 생성될 테이블 이름을 지정합니다.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID 값을 자동으로 증가시키는 전략을 사용합니다.
    private Long id;

    @Column(nullable = false) // 이름은 필수입니다.
    private String name;

    @Column(unique = true, nullable = false) // 이메일은 유니크하며 필수입니다.
    private String email;

    private String phone;

    private LocalDate birthdate;

    private String gender;

    private String address;

    @Column(nullable = false)
    private String password;

    @Transient // confirmPassword는 데이터베이스에 저장되지 않습니다.
    private String confirmPassword;


    // Getter, Setter 및 기타 필요한 메서드
}
