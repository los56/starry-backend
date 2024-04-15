package team.ubox.starry.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import team.ubox.starry.repository.entity.redis.ChatMessage;
import team.ubox.starry.helper.UUIDHelper;

import java.time.Instant;
import java.util.UUID;

public class ChatMessageTest {

    @Test
    void WriterEquals() {
        String writerID = UUIDHelper.UUIDToString(UUID.randomUUID());
        String sessionId = UUIDHelper.UUIDToString(UUID.randomUUID());
        ChatMessage.Sender original = new ChatMessage.Sender(writerID, sessionId, "닉네임");

        ChatMessage.Sender sameSender = new ChatMessage.Sender(writerID, sessionId, "닉네임");
        Assertions.assertEquals(original, sameSender);

        ChatMessage.Sender diffId = new ChatMessage.Sender(UUIDHelper.UUIDToString(UUID.randomUUID()), sessionId, "닉네임");
        Assertions.assertNotEquals(original, diffId);

        ChatMessage.Sender diffSessionId = new ChatMessage.Sender(writerID, UUIDHelper.UUIDToString(UUID.randomUUID()), "닉네임");
        Assertions.assertNotEquals(original, diffSessionId);

        // Different nickname is ok
        ChatMessage.Sender diffNickname = new ChatMessage.Sender(writerID, sessionId, "닉네임1");
        Assertions.assertEquals(original, diffNickname);
    }

    @Test
    void ChatMessageEquals() {
        String roomId = UUIDHelper.UUIDToString(UUID.randomUUID());
        String writerID = UUIDHelper.UUIDToString(UUID.randomUUID());
        String sessionId = UUIDHelper.UUIDToString(UUID.randomUUID());
        ChatMessage.Sender sender = new ChatMessage.Sender(writerID, sessionId, "닉네임");

        long writeTime = Instant.now().toEpochMilli();
        ChatMessage original = new ChatMessage(roomId, sender, "콘텐츠", writeTime, false);
        ChatMessage copy = new ChatMessage(roomId, sender, "콘텐츠", writeTime, false);

        Assertions.assertEquals(original, copy);
    }
}
