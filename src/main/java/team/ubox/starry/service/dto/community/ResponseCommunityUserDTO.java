package team.ubox.starry.service.dto.community;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.ubox.starry.repository.entity.User;

@Getter
@Setter
@NoArgsConstructor
public class ResponseCommunityUserDTO {
    private String nickname;
    private String profileImageUrl;

    public static ResponseCommunityUserDTO from(User entity) {
        ResponseCommunityUserDTO instance = new ResponseCommunityUserDTO();

        instance.nickname = entity.getNickname();
        instance.profileImageUrl = entity.getProfileImageUrl();

        return instance;
    }
}
