package team.ubox.starry.service.dto.chat;

import lombok.*;

import java.sql.Timestamp;

public class ChatDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class SendMessage {
        private SenderDTO sender;
        private String content;
        private Timestamp sendDate;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class BroadCastMessage {
        private SenderDTO sender;
        private String content;
        private Timestamp sendDate;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SenderDTO {
        private String id;
        private String nickname;
    }
}
