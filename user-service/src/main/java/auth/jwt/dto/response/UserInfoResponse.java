package auth.jwt.dto.response;

import auth.jwt.domain.user.Role;

public record UserInfoResponse(
        String email,
        String username

) {
}
