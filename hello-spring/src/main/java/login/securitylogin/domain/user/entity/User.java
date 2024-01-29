package login.securitylogin.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails { // UserDetails를 상속받음 -> 해당 객체는 Spring Security를 활용한 인증 객체로 활용

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Builder // Builder 생성자
    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public User update(String nickname){
        this.nickname = nickname;

        return this;
    }


    @Override // 권한 반환 메서드
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //사용자가 가지고 있는 권한 리스트 반환 -> "user" 권한 반환
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override // 사용자 식별 값 반환 메서드
    public String getUsername() {
        return email;
    }

    @Override // 사용자의 비밀번호 반환 -> 암호화 해서 저장되어 있으므로, 암호화 값 반환
    public String getPassword() {
        return password;
    }

    @Override // 계정 만료 확인 메서드 -> 계정 만료 x일 시, true 반환
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override // 계정 잠금 확인 메서드 -> 계정 잠금 x일 시, true 반환
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override // 비밀번호 만료 확인 메서드 -> 비밀번호 만료 x일 시, true 반환
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override // 계정 사용가능 확인 메서드 -> 계정 사용 가능일 시, true 반환
    public boolean isEnabled() {
        return true;
    }
}
