package team.ubox.starry.repository.redis;

import team.ubox.starry.repository.entity.redis.ChatMessage;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRedisRepository {
    ChatMessage push(String roomId, ChatMessage message);
    List<ChatMessage> get(String roomId, Integer count);
    //List<ChatMessage> pop(String roomId, Integer count);
    List<ChatMessage> remove(String roomId);

    Optional<Long> findMessageIndex(String roomId, ChatMessage message);
    Optional<ChatMessage> updateMessage(String roomId, ChatMessage before, ChatMessage after);

    Optional<ChatMessage> getFromIndex(String roomId, Long index);
}
