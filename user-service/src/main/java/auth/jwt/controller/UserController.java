package auth.jwt.controller;

import auth.jwt.dto.request.JoinRequest;
import auth.jwt.dto.request.LoginRequest;
import auth.jwt.dto.request.PwChangeRequest;
import auth.jwt.dto.request.TokenRefreshRequest;
import auth.jwt.dto.response.CodeMessageResponse;
import auth.jwt.dto.response.DataResponse;
import auth.jwt.dto.response.LoginResponse;
import auth.jwt.dto.response.UserInfoResponse;
import auth.jwt.exception.ExceptionStatusProvider;
import auth.jwt.service.UserService;
import auth.jwt.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user-service")
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;

    /**
     * 회원가입
     */
    @PostMapping("/join") //@RequestBody -> json 통해 받아온 정보
    public DataResponse<JoinRequest> joinPost(@Validated @RequestBody JoinRequest request, Errors errors) {
        if (errors.hasErrors()){ // 회원가입 시 에러가 발생하는 경우 (ex. 폼 미작성, 이메일 형식 미준수)
            ExceptionStatusProvider.throwError(errors);
        }
        return responseService.getResultResponse(userService.join(request));
    }


    /**
     * 로그인
     * @return 토큰 발급
     */
    @PostMapping("/login")
    public DataResponse<LoginResponse> loginPost(@Validated @RequestBody LoginRequest request, BindingResult result,  Errors errors){
        if (errors.hasErrors()) {
            ExceptionStatusProvider.throwError(errors);
        }
        return responseService.getResultResponse(userService.login(request));
    }


    @GetMapping("/token/profile")
    public DataResponse<UserInfoResponse> getUserInfo(@RequestHeader("ACCESS_TOKEN") String accessToken) {
        return responseService.getResultResponse(userService.userInfoResponse(accessToken));
    }

    /**
     * 토큰 재발급
     */
    @PostMapping("/token/access-refresh")
    public DataResponse<LoginResponse> accessRefreshToken(@Validated @RequestBody TokenRefreshRequest request, Errors errors) {

        if (errors.hasErrors()) {
            ExceptionStatusProvider.throwError(errors);
        }
        return responseService.getResultResponse(userService.getAccessTokenRefresh(request));
    }

    @PostMapping("/logout")
    public CodeMessageResponse logoutPost(@Validated @RequestBody LoginResponse response,Errors errors) {
        // access token 검증
        if (errors.hasErrors()) {
            ExceptionStatusProvider.throwError(errors);
        }
        userService.logout(response);

        return responseService.getSuccessResponse();
    }

    @PostMapping("/password-check")
    public CodeMessageResponse pwCheckPost(@Validated @RequestBody PwChangeRequest request, Errors errors) {
        if (errors.hasErrors()) {
            ExceptionStatusProvider.throwError(errors);
        }
        log.info("야호={}", request);
        userService.pwCheck(request);

        return responseService.getSuccessResponse();
    }

    @PostMapping("/password-change")
    public CodeMessageResponse pwChangePost(@Validated @RequestBody PwChangeRequest request, Errors errors) {
        if (errors.hasErrors()) {
            ExceptionStatusProvider.throwError(errors);
        }
        userService.pwChange(request);

        return responseService.getSuccessResponse();
    }

}
