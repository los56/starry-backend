package team.ubox.starry.repository.redis;

import org.springframework.data.repository.CrudRepository;
import team.ubox.starry.repository.entity.redis.StreamRedis;

import java.util.UUID;

public interface StreamRedisRepository extends CrudRepository<StreamRedis, UUID> {
}
