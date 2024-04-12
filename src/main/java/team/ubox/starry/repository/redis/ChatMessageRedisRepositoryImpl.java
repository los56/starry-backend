package team.ubox.starry.repository.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import team.ubox.starry.repository.entity.redis.ChatMessage;

import java.util.List;
import java.util.Optional;

@Repository
public class ChatMessageRedisRepositoryImpl implements ChatMessageRedisRepository {
    private final RedisTemplate<String, ChatMessage> redisTemplate;

    @Autowired
    public ChatMessageRedisRepositoryImpl(RedisTemplate<String, ChatMessage> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public ChatMessage push(String roomId, ChatMessage message) {
        redisTemplate.opsForList().leftPush(roomId, message);
        return message;
    }

    @Override
    public List<ChatMessage> get(String roomId, Integer count) {
        return redisTemplate.opsForList().range(roomId, 0, count);
    }

    @Override
    public List<ChatMessage> remove(String roomId) {
        List<ChatMessage> messageList = redisTemplate.opsForList().range(roomId, 0, -1);
        redisTemplate.delete(roomId);
        return messageList;
    }

    @Override
    public Optional<Long> findMessageIndex(String roomId, ChatMessage chatMessage) {
        return Optional.ofNullable(redisTemplate.opsForList().indexOf(roomId, chatMessage));
    }

    @Override
    public Optional<ChatMessage> updateMessage(String roomId, ChatMessage before, ChatMessage after) {
        Long index = redisTemplate.opsForList().indexOf(roomId, before);
        if(index == null) {
            return Optional.empty();
        }

        redisTemplate.opsForList().set(roomId, index, after);
        return Optional.of(after);
    }

    @Override
    public Optional<ChatMessage> getFromIndex(String roomId, Long index) {
        return Optional.ofNullable(redisTemplate.opsForList().index(roomId, index));
    }
}
