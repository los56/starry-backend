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
        ChatMessage.Writer original = new ChatMessage.Writer(writerID, sessionId, "닉네임");

        ChatMessage.Writer sameWriter = new ChatMessage.Writer(writerID, sessionId, "닉네임");
        Assertions.assertEquals(original, sameWriter);

        ChatMessage.Writer diffId = new ChatMessage.Writer(UUIDHelper.UUIDToString(UUID.randomUUID()), sessionId, "닉네임");
        Assertions.assertNotEquals(original, diffId);

        ChatMessage.Writer diffSessionId = new ChatMessage.Writer(writerID, UUIDHelper.UUIDToString(UUID.randomUUID()), "닉네임");
        Assertions.assertNotEquals(original, diffSessionId);

        // Different nickname is ok
        ChatMessage.Writer diffNickname = new ChatMessage.Writer(writerID, sessionId, "닉네임1");
        Assertions.assertEquals(original, diffNickname);
    }

    @Test
    void ChatMessageEquals() {
        String roomId = UUIDHelper.UUIDToString(UUID.randomUUID());
        String writerID = UUIDHelper.UUIDToString(UUID.randomUUID());
        String sessionId = UUIDHelper.UUIDToString(UUID.randomUUID());
        ChatMessage.Writer writer = new ChatMessage.Writer(writerID, sessionId, "닉네임");

        long writeTime = Instant.now().toEpochMilli();
        ChatMessage original = new ChatMessage(roomId, writer, "콘텐츠", writeTime, false);
        ChatMessage copy = new ChatMessage(roomId, writer, "콘텐츠", writeTime, false);

        Assertions.assertEquals(original, copy);
    }
}
