package auth.jwt.domain.user;

import auth.jwt.dto.request.JoinRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
//@Table(name="user")
@Table(name = "\"User\"")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // TODO IDENTITY로 한 이유
    @Column(name="user_id")
    private Long id;

    @Column(name="user_email", nullable = false, unique = true)
    private String email;

//    @Column(columnDefinition = "CHAR(64)", nullable = false) // TODO 64bit 이상 느낌?
    private String password;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Role role;

//    @OneToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL) // User 엔티티 저장 및 삭제 시 SaltPasswordEh 같이
//    @JoinColumn(name="salt_password_id")

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "salt_password_id", referencedColumnName = "salt_password_id")
    private SaltPassword saltPassword;



    protected User() { // @Builder 사용 위해 기본 생성자 추가

    }

    /**
     * 생성 메서드
     */
    // @TODO Builder 사용 고려
    public static User createUser(String email, String password, String username, Role role, SaltPassword saltPassword) {
        return User.builder()
                .email(email)
                .password(password)
                .username(username)
                .saltPassword(saltPassword)
                .role(role)
                .build();
    }
}
