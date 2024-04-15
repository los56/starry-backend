package team.ubox.starry.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team.ubox.starry.repository.entity.redis.ChatMessage;
import team.ubox.starry.repository.redis.ChatMessageRedisRepository;
import team.ubox.starry.repository.redis.RedisLockRepository;
import team.ubox.starry.helper.UUIDHelper;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class ChatMessageRedisRepositoryTest {
    private final ChatMessageRedisRepository chatMessageRedisRepository;
    private final RedisLockRepository redisLockRepository;

    @Autowired
    public ChatMessageRedisRepositoryTest(ChatMessageRedisRepository chatMessageRedisRepository, RedisLockRepository redisLockRepository) {
        this.chatMessageRedisRepository = chatMessageRedisRepository;
        this.redisLockRepository = redisLockRepository;
    }

    @Test
    void findTest() {
        final String roomId = UUIDHelper.UUIDToString(UUID.randomUUID());

        ChatMessage target = null;

        for(int i = 0;i < 100;i++) {
            ChatMessage.Sender sender = new ChatMessage.Sender(UUIDHelper.UUIDToString(UUID.randomUUID()), UUIDHelper.UUIDToString(UUID.randomUUID()), "테스트" + i);
            ChatMessage message = new ChatMessage(roomId, sender, "메시지 " + i, Instant.now().toEpochMilli(), false);
            chatMessageRedisRepository.push(roomId, message);

            if(i == 50) {
                target = message;
            }
        }

        assert target != null;
        ChatMessage desireMessage = new ChatMessage(target.getRoomId(), target.getSender(), target.getContent(), target.getSendTime(), false);
        long idx = chatMessageRedisRepository.findMessageIndex(roomId, desireMessage).get();

        ChatMessage message = chatMessageRedisRepository.getFromIndex(roomId, idx).get();
        Assertions.assertEquals(target, message);
    }



    /**
     *  채팅이 계속 생성되는 동안 수정(블라인드) 할 수 있도록 Locking Test
     * **/
    @Test
    void FindDuringMultipleMessageReceive() throws InterruptedException {
        int messageCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch cdl1 = new CountDownLatch(messageCount);

        String roomId = UUIDHelper.UUIDToString(UUID.randomUUID());
        String writerID = UUIDHelper.UUIDToString(UUID.randomUUID());
        String sessionId = UUIDHelper.UUIDToString(UUID.randomUUID());
        long writeTime = Instant.now().toEpochMilli();

        ChatMessage.Sender sender = new ChatMessage.Sender(writerID, sessionId, "닉네임");

        ChatMessage targetMessage = new ChatMessage(roomId, sender, "콘텐츠", writeTime, false);
        ChatMessage replacedMessage = new ChatMessage(roomId, sender, "수정된 콘텐츠", writeTime, true);

        // 더미 데이터 비동기 삽입
        for(int i = 0;i < messageCount;i++) {
            ChatMessage message = genNewMessage(i);
            executorService.submit(() -> {
                try {
                    while(!redisLockRepository.lock(roomId)) {
                        Thread.sleep(10);
                    }
                    chatMessageRedisRepository.push(roomId, message);
                    redisLockRepository.unlock(roomId);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    cdl1.countDown();
                }
            });

        }

        // 변경 대상 값 삽입
        while(!redisLockRepository.lock(roomId)) {
            Thread.sleep(10);
        }
        chatMessageRedisRepository.push(roomId, targetMessage);
        cdl1.await();

        CountDownLatch cdl2 = new CountDownLatch(messageCount);
        for(int i = 100;i < messageCount + 100;i++) {
            ChatMessage message = genNewMessage(i);
            executorService.submit(() -> {
                try {
                    while(!redisLockRepository.lock(roomId)) {
                        Thread.sleep(10);
                    }
                    chatMessageRedisRepository.push(roomId, message);
                    redisLockRepository.unlock(roomId);
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                finally {
                    cdl2.countDown();
                }
            });
        }

        while(!redisLockRepository.lock(roomId)) {
            Thread.sleep(10);
        }
        chatMessageRedisRepository.updateMessage(roomId, targetMessage, replacedMessage);
        redisLockRepository.unlock(roomId);

        cdl2.await();

        Long endIdx = chatMessageRedisRepository.findMessageIndex(roomId, replacedMessage).get();
        ChatMessage foundEnded = chatMessageRedisRepository.getFromIndex(roomId, endIdx).get();

        System.out.printf("%s - %s - %s%n", foundEnded.getSender().getId(), foundEnded.getContent(), foundEnded.getSendTime());

        List<ChatMessage> allMessages = chatMessageRedisRepository.get(roomId, 200).stream().sorted().toList();
        for (ChatMessage message : allMessages) {
            System.out.printf("%s - %s - %s%n", message.getSender().getId(), message.getContent(), message.getSendTime());
        }

        Assertions.assertEquals(1, allMessages.stream().filter(v -> v.getSender().getId().equals(writerID)).toArray().length);
        Assertions.assertEquals(201, allMessages.size());
    }

    private ChatMessage genNewMessage(int index) {
        String roomId = UUIDHelper.UUIDToString(UUID.randomUUID());
        String writerID = UUIDHelper.UUIDToString(UUID.randomUUID());
        String sessionId = UUIDHelper.UUIDToString(UUID.randomUUID());
        ChatMessage.Sender sender = new ChatMessage.Sender(writerID, sessionId, "닉네임 " + index);
        long writeTime = Instant.now().toEpochMilli();

        return new ChatMessage(roomId, sender, "콘텐츠 " + index, writeTime, false);
    }
}
