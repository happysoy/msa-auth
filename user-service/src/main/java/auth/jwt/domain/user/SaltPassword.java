package auth.jwt.domain.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name="saltPassword")
public class SaltPassword {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="salt_password_id")
    private Long id;

    // TODO -> JsonIgnore?
    @OneToOne(mappedBy = "saltPassword", fetch = FetchType.LAZY)
    private User user;

    // 유저 고유의 salt 값
    @Column(nullable = false)
    private String salt;

    /**
     * 비즈니스 로직
     */
    //Salt 값 생성
//    public static String createSalt() {
//        return PasswordEncoder.getSalt();
//    }
}
