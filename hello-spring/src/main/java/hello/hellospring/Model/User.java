package hello.hellospring.Model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "members") // 데이터베이스에 생성될 테이블 이름을 지정합니다.
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID 값을 자동으로 증가시키는 전략을 사용합니다.
    @Column(name ="id", updatable = false)
    private Long id;

    @Column(nullable = false) // 이름은 필수입니다.
    private String name;

    @Column(name = "email", unique = true, nullable = false) // 이메일은 유니크하며 필수입니다.
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "gender")
    private String gender;

    @Column(name = "address")
    private String address;

    @Column(name = "password", nullable = false)
    private String password;

    @Builder
    public User(Long id, String name, String email, String phone, LocalDate birthdate, String gender, String address, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birthdate = birthdate;
        this.gender = gender;
        this.address = address;
        this.password = password;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    // Getter, Setter 및 기타 필요한 메서드
}
