package team.ubox.starry.service.dto.chat;

import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

public class ChatDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class SendMessage {
        private String roomId;
        private String senderId;
        private String content;
        //private Timestamp sendDate;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class BroadCastMessage {
        private String roomId;
        private SenderDTO sender;
        private String content;
        private Long sendTime;

        public static int compare(BroadCastMessage left, BroadCastMessage right) {
            if(left.sendTime <= right.sendTime) {
                return -1;
            }
            return 1;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SenderDTO implements Serializable {
        private String id;
        private String nickname;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseAssignDTO {
        private String accessToken;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestJoinDTO {
        private String channelId;
        private String roomId;
        private String accessToken;
    }
}
