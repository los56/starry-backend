package team.ubox.starry.service.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import team.ubox.starry.repository.entity.User;

import java.sql.Timestamp;

public class UserDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class Response {
        private String id;
        private String username;
        private String nickname;
        private Timestamp passwordChangeDate;

        public static Response from(User entity) {
            return Response.builder()
                    .id(entity.getIdString())
                    .username(entity.getUsername())
                    .nickname(entity.getNickname())
                    .passwordChangeDate(entity.getPasswordChangeDate())
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequestChangeInfo {
        @NotBlank
        @Length(min = 2, max = 64)
        private String nickname;

        @URL
        @Null
        private String profileImageUrl;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequestChangePassword {
        @NotBlank
        private String currentPassword;

        @NotBlank
        @Length(min = 8, max = 256)
        private String newPassword;
    }
}
