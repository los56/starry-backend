package team.ubox.starry.dto.user;

import lombok.*;
import team.ubox.starry.entity.User;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ResponseUserDTO {
    private String id;
    private String username;
    private String nickname;
    private Timestamp passwordChangeDate;

    public static ResponseUserDTO from(User entity) {
        return ResponseUserDTO.builder()
                .id(entity.getIdString())
                .username(entity.getUsername())
                .nickname(entity.getNickname())
                .passwordChangeDate(entity.getPasswordChangeDate())
                .build();

    }
}
