package team.ubox.starry.service.dto.channel;

import lombok.*;
import team.ubox.starry.repository.entity.Channel;
import team.ubox.starry.helper.UUIDHelper;

public class ChannelDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response {
        private String id;
        private String nickname;
        private String description;
        private Boolean verified;
        private String profileImageUrl;
        private String bannerUrl;

        private Integer followers = 0;

        public static Response from(Channel entity) {
            Response instance = new Response();

            instance.setId(UUIDHelper.UUIDToString(entity.getOwner().getId()));
            instance.setNickname(entity.getOwner().getNickname());
            instance.setDescription(entity.getDescription());
            instance.setVerified(entity.getVerified());
            instance.setProfileImageUrl(entity.getOwner().getProfileImageUrl());
            instance.setBannerUrl(entity.getBannerUrl());

            return instance;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class ResponseRelation {
        private String channelId;
        private Boolean following;
    }
}
