package auth.jwt.service;

import auth.jwt.config.jwt.JwtProvider;
import auth.jwt.domain.user.User;
import auth.jwt.domain.user.Role;
import auth.jwt.domain.user.SaltPassword;
import auth.jwt.dto.request.JoinRequest;
import auth.jwt.dto.request.LoginRequest;
import auth.jwt.dto.request.PwChangeRequest;
import auth.jwt.dto.request.TokenRefreshRequest;
import auth.jwt.dto.response.LoginResponse;
import auth.jwt.dto.response.UserInfoResponse;
import auth.jwt.exception.ExceptionStatus;
import auth.jwt.exception.GlobalException;
import auth.jwt.repository.UserRepository;
import auth.jwt.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private static final long REFRESH_TOKEN_DURATION = 3;

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;
    PasswordEncoder passwordEncoder = new PasswordEncoder();

    /**
     * 회원가입
     * MySQL 에 유저 정보 저장
     */
    @Transactional
    public JoinRequest join(JoinRequest request) {
        // 비밀번호와 비밀번호 확인 일치 검증
        if (!request.getPassword().equals(request.getPasswordCheck())) {
            throw new GlobalException(ExceptionStatus.FAIL_PASSWORD_CHECK);
        }

        // 이메일 중복 검증
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new GlobalException(ExceptionStatus.DUPLICATE_EMAIL);
        }

        // 비밀번호 암호화 로직을 User 클래스가 아닌 서비스 계층에서 처리
        // 1. User 클래스 단일 책임 원칙
        // 2. Test의 용이성 -> 클래스를 테스트할 때, 만약 서비스에서 처리하면 테스트마다 패스워드를 해싱해야 함
        String salt = passwordEncoder.getSalt(); // TODO 싱글톤 관리 -> PasswordEncoder 위에서 하나의 인스턴스로 선언해야 하지 않나??
        String digest = passwordEncoder.getEncryption(salt, request.getPassword());
        request.setPassword(digest); // 해싱된 비밀번호로 변경하기 위해 JoinRequestDTO는 class로 설계

        // SaltPassword 정보 생성
        SaltPassword saltPassword = new SaltPassword();
        saltPassword.setSalt(salt);

        // User 생성
        User user = User.createUser(request.getEmail(), request.getPassword(), request.getUsername(), Role.USER, saltPassword);

        // JPA 설정(email column unique=true)을 통해 동일 이메일에 대한 회원가입 요청 처리
        userRepository.save(user);

        return request;
    }


    /**
     * 로그인
     * Redis 에 refreshToken 저장
     */
    @Transactional
    public LoginResponse login(LoginRequest request) {
        // DB 에서 클라이언트 조회
        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new GlobalException(ExceptionStatus.FAIL_LOGIN));

        String salt = user.getSaltPassword().getSalt();

        // 클라이언트 인증
        if (!passwordEncoder.isSameCheck(user.getPassword(), request.password(), salt)){
            throw new GlobalException(ExceptionStatus.FAIL_LOGIN);
        }

        // TODO 로그아웃 된 토큰인지 확인(blacklist에 있는지)
        String refreshToken = jwtProvider.createRefreshToken(request.email());

        // redis 에 저장
        // key: email, value: refreshToken -> refresh token 이 있는지 찾기 위해서 모든 값들을 다 확인해야 한다
        // key: refreshToken, value: email -> 시간복잡도 (O(1))
        redisService.setRedisTemplate(refreshToken, user.getEmail(), new Date()); // TTL 기능을 통해 유효시간 설정 -> 3일

        return LoginResponse.builder()
                .accessToken(jwtProvider.createAccessToken(user.getEmail(), user.getRole()))
                .refreshToken(refreshToken)
                .build();

    }

    /**
     * 로그아웃
     * RefreshToken 삭제
     */
    @Transactional
    public void logout(LoginResponse request) {
        // access token 유효성 검증
        String accessToken = request.accessToken();

        String redisKey = request.refreshToken();
        // refresh token 삭제
        if (redisService.getRedisTemplateValue(redisKey) != null) {
            redisService.deleteRedisTemplateValue(redisKey);
        }

        // access token 유효시간과 함께 BlackList에 저장
        redisService.setRedisTemplate(accessToken, "logout", jwtProvider.getExpirationDate(accessToken)); // TTL 기능을 통해 유효시간 설정 -> 3일
    }

    /**
     *  유저 정보 조회
     *  @return username, email
     */
    public UserInfoResponse userInfoResponse(String accessToken) {

        if (accessToken.equals("null")) { // String 으로 받아와서 그런지 "null"이라고 해줘야 한다
            throw new GlobalException(ExceptionStatus.EMPTY_ACCESS_TOKEN);
        }

        String email = jwtProvider.getEmailByToken(accessToken);

        User user = userRepository.findByEmail(email).orElseThrow(() -> new GlobalException(ExceptionStatus.EMPTY_USER));

        return new UserInfoResponse(user.getEmail(), user.getUsername()); // TODO 왜 new 메소드를 안붙이면 에러가 날까?
    }


    /**
     * refreshToken 이 유효한 경우 accessToken 재발급
     */
    @Transactional
    public LoginResponse getAccessTokenRefresh (TokenRefreshRequest loginResponse) {
        String accessToken = loginResponse.accessToken();
        String refreshToken = loginResponse.refreshToken();

        // accessToken 에서 이메일 추출
        String email = jwtProvider.getEmailByToken(accessToken);

        // 회원가입 하지 않은 회원 처리
        User user = userRepository.findByEmail(email).orElseThrow(() -> new GlobalException(ExceptionStatus.EMPTY_USER));

        // redis 에 refreshToken 이 존재하지 않는 경우(만료) 예외 처리
        if (!redisService.isExistKey(refreshToken)) {
            log.info("토큰이 존재하지 않습니다");
            throw new GlobalException(ExceptionStatus.INVALID_TOKEN);
        }

        // accessToken 재발급
        return LoginResponse.builder()
                .accessToken(jwtProvider.createAccessToken(email, user.getRole()))
                .refreshToken(refreshToken)
                .build();
    }


    /**
     * 전체 유저 조회 TODO 유저 리스트 조회 -> 페이징
     */
    public List<UserInfoResponse> getUserList() {
        return userRepository.findAllByRole(Role.USER);
    }


    public void pwCheck(PwChangeRequest request) {
        String accessToken = request.accessToken();

        if (accessToken.equals("null")) { // String 으로 받아와서 "null"이라고 해줘야 한다
            throw new GlobalException(ExceptionStatus.EMPTY_ACCESS_TOKEN);
        }

        String email = jwtProvider.getEmailByToken(accessToken);

        // DB 에서 클라이언트 조회
        User user = userRepository.findByEmail(email).orElseThrow(() -> new GlobalException(ExceptionStatus.EMPTY_USER));

        String salt = user.getSaltPassword().getSalt();

        // 클라이언트 인증
        if (!passwordEncoder.isSameCheck(user.getPassword(), request.password(), salt)){
            throw new GlobalException(ExceptionStatus.FAIL_PW_CHECK);
        }


    }


    @Transactional
    public void pwChange(PwChangeRequest request) {
        String accessToken = request.accessToken();

        if (accessToken.equals("null")) { // String 으로 받아와서 "null"이라고 해줘야 한다
            throw new GlobalException(ExceptionStatus.EMPTY_ACCESS_TOKEN);
        }

        String email = jwtProvider.getEmailByToken(accessToken);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new GlobalException(ExceptionStatus.EMPTY_USER));

        // salt 값 새로 생성
        String salt = passwordEncoder.getSalt();
        String digest = passwordEncoder.getEncryption(salt, request.password());

        SaltPassword saltPassword = user.getSaltPassword();
        saltPassword.setSalt(salt);

        user.setSaltPassword(saltPassword);
        user.setPassword(digest);


        userRepository.save(user);
    }

}
