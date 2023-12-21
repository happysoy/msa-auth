package auth.jwt.dto.response;

public record DataResponse<T>(Boolean isSuccess, int code, String message, T result) {

    public static <T> DataResponse<T> of(Boolean isSuccess, int code, String message, T result) {
        return new DataResponse<>(isSuccess, code, message, result);
    }
}