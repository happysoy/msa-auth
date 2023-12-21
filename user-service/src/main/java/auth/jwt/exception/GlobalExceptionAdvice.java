package auth.jwt.exception;


import auth.jwt.dto.response.CodeMessageResponse;
import auth.jwt.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 한곳에서 관리
@RequiredArgsConstructor
public class GlobalExceptionAdvice {
    private final ResponseService responseService;

    @ExceptionHandler(GlobalException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CodeMessageResponse globalException(GlobalException globalException) {
        ExceptionStatus status = globalException.getExceptionStatus();
        return responseService.getExceptionResponse(status);
    }
}
