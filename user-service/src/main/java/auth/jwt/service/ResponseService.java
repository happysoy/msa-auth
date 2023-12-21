package auth.jwt.service;

import auth.jwt.dto.response.CodeMessageResponse;
import auth.jwt.dto.response.DataResponse;
import auth.jwt.exception.ExceptionStatus;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {

    public CodeMessageResponse getSuccessResponse() {
        return CodeMessageResponse.of(true, 1000, "요청 성공");
    }

    public CodeMessageResponse getFailResponse() {
        return CodeMessageResponse.of(false, 4000, "요청 실패");

    }

    public <T> DataResponse<T> getResultResponse(T data) {
        return DataResponse.of(true, 1000, "요청 성공", data);
    }

    public CodeMessageResponse getExceptionResponse(ExceptionStatus status) {
        return CodeMessageResponse.of(status.isSuccess(), status.getCode(), status.getMessage());
    }
}
