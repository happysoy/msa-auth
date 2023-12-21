package auth.jwt.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record LoginResponse(
        String accessToken,
        String refreshToken

) {
}
