package team.ubox.starry.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class RequestRegisterDTO {

    @NotBlank
    @Length(min = 4, max = 128)
    private String username;

    @NotBlank
    @Length(min = 8)
    private String password;

    @NotBlank
    @Length(min = 4, max = 64)
    private String nickname;

    @NotBlank
    @Email
    private String email;
}
