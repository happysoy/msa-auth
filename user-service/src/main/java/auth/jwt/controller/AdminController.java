package auth.jwt.controller;

import auth.jwt.dto.response.DataResponse;
import auth.jwt.dto.response.UserInfoResponse;
import auth.jwt.service.UserService;
import auth.jwt.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user-service/admin")
public class AdminController {
    /**
     * 유저 관리
     */

    private final UserService userService;
    private final ResponseService responseService;

    // TODO 페이징으로 처리하기
    @GetMapping("/users")
    public DataResponse<List<UserInfoResponse>> getMemberList() { // TODO <LIST<UserInfoResponse>>를 UserListResponse 레코드로 못바꾸나?
        return responseService.getResultResponse(userService.getUserList());
    }


}
