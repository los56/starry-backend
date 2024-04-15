package team.ubox.starry.service.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import team.ubox.starry.repository.entity.User;
import team.ubox.starry.helper.UUIDHelper;

public class RegisterDTO {
    @Getter
    @Setter
    public static class Request {

        @NotBlank
        @Length(min = 4, max = 128)
        private String username;

        @NotBlank
        @Length(min = 8)
        private String password;

        @NotBlank
        @Length(min = 2, max = 64)
        private String nickname;

        @NotBlank
        @Email
        private String email;
    }

    @Getter
    @Setter
    public static class Response {
        private String id;
        private String username;
        private String nickname;

        public static Response from(User entity) {
            Response instance = new Response();

            instance.setId(UUIDHelper.UUIDToString(entity.getId()));
            instance.setUsername(entity.getUsername());
            instance.setNickname(entity.getNickname());

            return instance;
        }
    }
}
