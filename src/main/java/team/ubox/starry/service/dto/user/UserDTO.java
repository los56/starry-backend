package team.ubox.starry.service.dto.user;

import lombok.*;
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
    @Builder
    @AllArgsConstructor
    public static class RequestChangeInfo {
        private String username;
        private String nickname;
    }
}
