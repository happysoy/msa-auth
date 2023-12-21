package auth.jwt.service;

import auth.jwt.domain.user.SaltPassword;
import auth.jwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class UserServiceTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Test
    @Rollback(false)
    void 회원가입() throws Exception{


        //given
        SaltPassword saltPassword = new SaltPassword();
//       User user = Member.createUser("userA", "email", "pw", saltPassword,  Role.USER);

        //when
//        userService.join(member);

        //then
//        User findUser = userRepository.findByEmail(member.getEmail()).orElse(null);
//        assertEquals(member.getUsername(), findMember.getUsername());
    }
//    @Test
//    void 중복회원_예외() throws Exception{
//        //given
//        SaltPassword saltPassword1 = new SaltPassword();
//        SaltPassword saltPassword2 = new SaltPassword();
//
//        Member member1 = Member.createMember("userA","email1", "pw", saltPassword1, Role.USER);
//        Member member2 = Member.createMember("userB","email1", "pw", saltPassword2, Role.USER);
//
//        //when
//        memberService.join(member1.getUsername(), member1.getEmail(), member1.getPassword(), member1.getPassword());
//
//        //then
//        assertThatThrownBy(()->memberService.join(member2.getUsername(), member2.getEmail(), member2.getPassword(), member2.getPassword()))
//                .isInstanceOf(DuplicateEmailException.class);
//    }
//    @Test
//    void 로그인() throws Exception{
//        //given
//        SaltPassword saltPassword = new SaltPassword();
//        Member member = Member.createMember("email", "pw", "userA", saltPassword,  Role.USER);
//        memberService.join(member.getUsername(), member.getEmail(), member.getPassword(), member.getPassword());
//
//        //when
//
//        Member findMember = memberRepository.findByEmail(member.getEmail()).orElse(null);
//        String salt = findMember.getSaltPassword().getSalt();
//        String digest = findMember.getSaltPassword().getDigest();
//
//        String encrypt = Encryption.getEncrypt(salt, member.getPassword());
//
//        // then
//        assertEquals(digest, encrypt);
//    }
}