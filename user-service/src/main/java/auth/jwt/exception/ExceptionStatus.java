package auth.jwt.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /**
     * 2100: request 에러
     */


    // 회원가입, 로그인
    USER_EMPTY_EMAIL(false, 2101, "이메일을 입력해주세요"),
    USER_EMPTY_USERNAME(false, 2102, "이름을 입력해주세요"),
    USER_EMPTY_PASSWORD(false, 2103, "비밀번호를 입력해주세요"),

    USER_INVALID_EMAIL(false, 2104, "이메일 형식이 올바르지 않습니다"),
    USER_INVALID_PASSWORD(false, 2105, "비밀번호 형식이 올바르지 않습니다"),

    // role 관리
    USER_ACCESS_DENIED(false, 2100, "접근 권한이 없습니다"),

    // token 관리
    INVALID_TOKEN(false, 2200, "유효하지 않은 토큰입니다"),
    EMPTY_ACCESS_TOKEN(false, 2203, "Access Token 이 없습니다"),
    EMPTY_REFRESH_TOKEN(false, 2203, "Refresh Token 이 없습니다"),

    /**
     * 2200: response 에러
     */
    RESPONSE_ERROR(false, 2200, "값을 불러오는데 실패하였습니다."),
    FAIL_PASSWORD_CHECK(false, 2001, "비밀번호와 비밀번호 확인이 일치하지 않습니다"),
    FAIL_LOGIN(false, 2202, "아이디 또는 비밀번호를 확인해주세요"),
    FAIL_PW_CHECK(false, 2202, "비밀번호가 올바르지 않습니다"),
    DUPLICATE_EMAIL(false, 2203, "중복된 이메일입니다"),
    EMPTY_USER(false, 2204, "존재하지 않는 사용자입니다"),
    ENCRYPT_ERROR(false, 2100, "암호화 알고리즘을 적용하지 못했습니다");



    private final boolean isSuccess;
    private final int code;
    private final String message;

}
