package team.ubox.starry.repository.entity.redis;

import lombok.*;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ChatMessage implements Comparable<ChatMessage>{
    private String roomId;
    private Writer writer;
    private String content;
    private Long writeTime;
    private Boolean blinded;

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ChatMessage target)) {
            return false;
        }
        return this.roomId.equals(target.roomId) && this.writer.equals(target.getWriter()) && this.writeTime.equals(target.getWriteTime());
    }

    @Override
    public int compareTo(ChatMessage o) {
        return writeTime.compareTo(o.getWriteTime());
    }

    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class Writer {
        private String id;
        private String sessionId;
        private String nickname;

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof Writer target)) {
                return false;
            }
            return this.id.equals(target.getId()) && this.sessionId.equals(target.getSessionId());
        }
    }
}
