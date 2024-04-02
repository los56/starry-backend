package team.ubox.starry.dto.user;

import lombok.Getter;
import lombok.Setter;
import team.ubox.starry.entity.User;
import team.ubox.starry.util.UUIDUtil;

@Getter
@Setter
public class ResponseRegisterDTO {
    private String id;
    private String username;
    private String nickname;

    public static ResponseRegisterDTO from(User entity) {
        ResponseRegisterDTO instance = new ResponseRegisterDTO();

        instance.setId(UUIDUtil.UUIDToString(entity.getId()));
        instance.setUsername(entity.getUsername());
        instance.setNickname(entity.getNickname());

        return instance;
    }
}
