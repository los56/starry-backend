package team.ubox.starry.repository.entity.redis;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import team.ubox.starry.repository.entity.User;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@RedisHash(value = "chat_session")
public class ChatSession {
    @Id
    private String id;

    private UUID ownerId;
    private String nickname;

    private String channelId;
    private String roomId;

    public void updateOwnerData(UUID ownerId, String nickname) {
        this.ownerId = ownerId;
        this.nickname = nickname;
    }
}


