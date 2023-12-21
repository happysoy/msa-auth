package auth.jwt.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class JoinRequest {
    @NotBlank(message = "이름을 입력해주세요")
    String username;
    @NotBlank(message = "이메일을 입력해주세요")
    String email;
    @NotBlank(message = "비밀번호를 입력해주세요")
    String password;
    @NotBlank(message = "비밀번호 확인을 입력해주세요")
    String passwordCheck;

}
