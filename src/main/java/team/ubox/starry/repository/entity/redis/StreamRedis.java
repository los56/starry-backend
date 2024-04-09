package team.ubox.starry.repository.entity.redis;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@RedisHash(value = "streams")
public class StreamRedis {
    @Id
    private UUID id;

    private String streamId;
    private String openTime;
}
