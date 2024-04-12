package team.ubox.starry.repository.redis;

import org.springframework.data.repository.CrudRepository;
import team.ubox.starry.repository.entity.redis.ChatSession;

import java.util.UUID;

public interface ChatSessionRedisRepository extends CrudRepository<ChatSession, String> {
}
