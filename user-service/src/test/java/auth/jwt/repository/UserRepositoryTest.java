package auth.jwt.repository;

import auth.jwt.domain.user.SaltPassword;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@SpringBootTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
//    @Rollback(value = false)
    @DisplayName("save")
    void save() throws Exception{
        //given
        SaltPassword saltPassword = new SaltPassword();
//        Member member = Member.createMember("userA", "email", "pw", saltPassword, Role.USER);

        //when
//        memberRepository.save(member);
//
//        //then
//        Member findMember = memberRepository.findById(member.getId()).orElse(null);
//        assertThat(member.getId()).isEqualTo(requireNonNull(findMember).getId());

    }

}