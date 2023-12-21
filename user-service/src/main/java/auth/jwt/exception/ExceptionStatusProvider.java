package auth.jwt.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;

@Slf4j
public class ExceptionStatusProvider {

    public static void throwError(Errors errors) {
        String code = errors.getFieldError().getCode();
        String field = errors.getFieldError().getField();
        log.info("code={}", code);
        log.info("field={}", field);
        throw new GlobalException(ExceptionStatusProvider.getExceptionStatus(code, field));
    }

    public static ExceptionStatus getExceptionStatus(String code, String target) {
        switch (code) {
            case "NotBlank" -> {
                switch (target) {
                    case "email" -> {
                        return ExceptionStatus.USER_EMPTY_EMAIL;
                    }
                    case "password" -> {
                        return ExceptionStatus.USER_EMPTY_PASSWORD;
                    }
                    case "username" -> {
                        return ExceptionStatus.USER_EMPTY_USERNAME;
                    }
                }
            }
            case "Pattern", "Length" -> {
                switch (target) {
                    case "email" -> {
                        return ExceptionStatus.USER_INVALID_EMAIL;
                    }
                    case "password" -> {
                        return ExceptionStatus.USER_INVALID_PASSWORD;
                    }
                }
            }
            case "NotNull" -> {
                switch (target){
                    case "accessToken" ->{
                        return ExceptionStatus.EMPTY_ACCESS_TOKEN;
                    }
                    case "refreshToken"->{
                        return ExceptionStatus.EMPTY_REFRESH_TOKEN;
                    }
                }
            }
//            case "JwtMalformed" -> {
//                return ExceptionStatus.INVALID_TOKEN;
//            }
        }
        return ExceptionStatus.RESPONSE_ERROR;
    }
}
