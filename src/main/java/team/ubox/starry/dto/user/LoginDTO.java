package team.ubox.starry.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

public class LoginDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Request {
        @NotBlank
        @Length(min = 4, max = 128)
        private String username;

        @NotBlank
        @Length(min = 8)
        private String password;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private String accessToken;
        private String refreshToken;
    }
}
