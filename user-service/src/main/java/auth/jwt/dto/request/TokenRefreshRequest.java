package auth.jwt.dto.request;

import jakarta.validation.constraints.NotNull;

public record TokenRefreshRequest(
        @NotNull
        String accessToken,
        @NotNull
        String refreshToken
) {
}
