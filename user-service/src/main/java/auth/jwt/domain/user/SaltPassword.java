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

    @OneToOne(mappedBy = "saltPassword", fetch = FetchType.LAZY)
    private User user;

    // 유저 고유의 salt 값
    @Column(nullable = false)
    private String salt;

}
