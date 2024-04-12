package team.ubox.starry.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.ubox.starry.exception.CustomError;
import team.ubox.starry.exception.CustomException;
import team.ubox.starry.helper.UUIDHelper;
import team.ubox.starry.repository.UserRepository;
import team.ubox.starry.repository.entity.User;
import team.ubox.starry.repository.entity.redis.ChatSession;
import team.ubox.starry.repository.entity.redis.StreamRedis;
import team.ubox.starry.repository.redis.ChatSessionRedisRepository;
import team.ubox.starry.repository.redis.RedisLockRepository;
import team.ubox.starry.repository.redis.StreamRedisRepository;
import team.ubox.starry.security.provider.JwtProvider;
import team.ubox.starry.service.dto.chat.ChatDTO;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final UserRepository userRepository;
    private final ChatSessionRedisRepository chatSessionRedisRepository;
    private final StreamRedisRepository streamRedisRepository;
    private final RedisLockRepository redisLockRepository;
    private final JwtProvider jwtProvider;

    public void joinUser(String sessionId, ChatDTO.RequestJoinDTO requestJoinDTO) {
        ChatSession chatSession = ChatSession.builder()
                .id(sessionId).roomId(requestJoinDTO.getRoomId()).channelId(requestJoinDTO.getChannelId()).build();

        if(requestJoinDTO.getAccessToken() != null && jwtProvider.validateToken(requestJoinDTO.getAccessToken())) {
            User userDetail = (User)jwtProvider.getAuthentication(requestJoinDTO.getAccessToken()).getPrincipal();
            User user = userRepository.findById(userDetail.getId()).orElseThrow(() -> new CustomException(CustomError.NOT_FOUND_USER));
            chatSession.updateOwnerData(user.getId(), user.getNickname());
        }

        chatSessionRedisRepository.save(chatSession);

        try {
            String lockKey = makeRedisLockKey(requestJoinDTO.getRoomId());
            while (!redisLockRepository.lock(lockKey)) {
                Thread.sleep(10);
            }

            Optional<StreamRedis> streamOptional = streamRedisRepository.findById(UUIDHelper.stringToUUID(chatSession.getChannelId()));
            if(streamOptional.isPresent()) {
                StreamRedis stream = streamOptional.get();
                stream.increaseViewers();
                streamRedisRepository.save(stream);
            }

            redisLockRepository.unlock(lockKey);
        } catch (InterruptedException e) {
            throw new CustomException(CustomError.INTERNAL_SERVER_ERROR);
        }
    }

    public void exitUser(String sessionId) {
        ChatSession chatSession = chatSessionRedisRepository.findById(sessionId).orElseThrow(() -> new CustomException(CustomError.NOT_FOUND_SESSION));
        
        try {
            String lockKey = makeRedisLockKey(chatSession.getRoomId());
            while (!redisLockRepository.lock(lockKey)) {
                Thread.sleep(10);
            }

            Optional<StreamRedis> streamOptional = streamRedisRepository.findById(UUIDHelper.stringToUUID(chatSession.getChannelId()));
            if(streamOptional.isPresent()) {
                StreamRedis stream = streamOptional.get();
                stream.decreaseViewers();
                streamRedisRepository.save(stream);
            }

            redisLockRepository.unlock(lockKey);
        } catch (InterruptedException e) {
            throw new CustomException(CustomError.INTERNAL_SERVER_ERROR);
        } finally {
            chatSessionRedisRepository.deleteById(sessionId);
        }
    }

    private String makeRedisLockKey(String roomId) {
        final String keyPrefix = "STREAM_";
        return keyPrefix + roomId;
    }
}
