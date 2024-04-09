package team.ubox.starry.repository.entity.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.web.socket.WebSocketSession;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@RedisHash("chat_session")
public class ChatSession {
    private UUID owner;
    private WebSocketSession session;
}
