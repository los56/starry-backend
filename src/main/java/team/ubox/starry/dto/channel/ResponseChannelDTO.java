package team.ubox.starry.dto.channel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.ubox.starry.entity.Channel;
import team.ubox.starry.util.UUIDUtil;

@Getter
@Setter
@NoArgsConstructor
public class ResponseChannelDTO {
    private String id;
    private String nickname;
    private String description;
    private Boolean verified;
    private String bannerUrl;

    public static ResponseChannelDTO from(Channel entity) {
        ResponseChannelDTO instance = new ResponseChannelDTO();

        instance.setId(UUIDUtil.UUIDToString(entity.getOwner().getId()));
        instance.setNickname(entity.getOwner().getNickname());
        instance.setDescription(entity.getDescription());
        instance.setVerified(entity.getVerified());
        instance.setBannerUrl(entity.getBannerUrl());

        return instance;
    }
}
