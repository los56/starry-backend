package team.ubox.starry.entity;

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
public class Stream {
    @Id
    private UUID id;

    private String streamId;
    private String openTime;
}
