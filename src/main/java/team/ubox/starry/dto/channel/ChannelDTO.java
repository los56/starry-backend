package team.ubox.starry.dto.channel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.ubox.starry.entity.Channel;
import team.ubox.starry.util.UUIDUtil;

public class ChannelDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response {
        private String id;
        private String nickname;
        private String description;
        private Boolean verified;
        private String bannerUrl;

        private Integer followers = 0;

        public static Response from(Channel entity) {
            Response instance = new Response();

            instance.setId(UUIDUtil.UUIDToString(entity.getOwner().getId()));
            instance.setNickname(entity.getOwner().getNickname());
            instance.setDescription(entity.getDescription());
            instance.setVerified(entity.getVerified());
            instance.setBannerUrl(entity.getBannerUrl());

            return instance;
        }
    }
}
