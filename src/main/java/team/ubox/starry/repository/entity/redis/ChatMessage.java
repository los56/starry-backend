package team.ubox.starry.repository.entity.redis;

import lombok.*;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ChatMessage implements Comparable<ChatMessage>{
    private String roomId;
    private Sender sender;
    private String content;
    private Long sendTime;
    private Boolean blinded;

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ChatMessage target)) {
            return false;
        }
        return this.roomId.equals(target.roomId) && this.sender.equals(target.getSender()) && this.sendTime.equals(target.getSendTime());
    }

    @Override
    public int compareTo(ChatMessage o) {
        return sendTime.compareTo(o.getSendTime());
    }

    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class Sender {
        private String id;
        private String sessionId;
        private String nickname;

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof Sender target)) {
                return false;
            }
            return this.id.equals(target.getId()) && this.sessionId.equals(target.getSessionId());
        }
    }
}
