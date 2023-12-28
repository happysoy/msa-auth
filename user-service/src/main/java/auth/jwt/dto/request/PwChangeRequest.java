package auth.jwt.dto.request;

import jakarta.validation.constraints.NotNull;

public record PwChangeRequest(
        @NotNull
        String accessToken,

        @NotNull
        String password

) {
}
