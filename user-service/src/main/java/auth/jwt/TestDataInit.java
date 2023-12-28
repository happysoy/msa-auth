package auth.jwt;


import auth.jwt.domain.user.Role;
import auth.jwt.domain.user.SaltPassword;
import auth.jwt.domain.user.User;
import auth.jwt.dto.request.JoinRequest;
import auth.jwt.repository.UserRepository;
import auth.jwt.util.PasswordEncoder;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInitAdmin();
        initService.dbInitUsers();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;
        PasswordEncoder passwordEncoder = new PasswordEncoder();

        public void dbInitAdmin() {
            extracted("a", "a", "관리자", Role.ADMIN);


        }

        public void dbInitUsers() {
            extracted("q", "q", "신짱구", Role.USER);
            extracted("w", "w", "신짱아", Role.USER);
            extracted("e", "e", "봉미선", Role.USER);
            extracted("r", "r", "신형만", Role.USER);

        }

        private void extracted(String email, String password, String username, Role role) {

            String salt = passwordEncoder.getSalt(); // TODO 싱글톤 관리 -> PasswordEncoder 위에서 하나의 인스턴스로 선언해야 하지 않나??
            String digest = passwordEncoder.getEncryption(salt, password);
            SaltPassword saltPassword = new SaltPassword();
            saltPassword.setSalt(salt);

            User user = User.createUser(email, digest, username, role, saltPassword);

            em.persist(user);
        }



    }
}
