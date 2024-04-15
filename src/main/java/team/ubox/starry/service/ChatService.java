package team.ubox.starry.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.ubox.starry.exception.CustomError;
import team.ubox.starry.exception.CustomException;
import team.ubox.starry.helper.UUIDHelper;
import team.ubox.starry.repository.UserRepository;
import team.ubox.starry.repository.entity.CustomUserDetail;
import team.ubox.starry.repository.entity.User;
import team.ubox.starry.repository.entity.redis.ChatMessage;
import team.ubox.starry.repository.entity.redis.ChatSession;
import team.ubox.starry.repository.entity.redis.StreamRedis;
import team.ubox.starry.repository.redis.ChatMessageRedisRepository;
import team.ubox.starry.repository.redis.ChatSessionRedisRepository;
import team.ubox.starry.repository.redis.RedisLockRepository;
import team.ubox.starry.repository.redis.StreamRedisRepository;
import team.ubox.starry.security.provider.JwtProvider;
import team.ubox.starry.service.dto.chat.ChatDTO;

import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final UserRepository userRepository;
    private final ChatSessionRedisRepository chatSessionRedisRepository;
    private final ChatMessageRedisRepository chatMessageRedisRepository;
    private final StreamRedisRepository streamRedisRepository;
    private final RedisLockRepository redisLockRepository;
    private final JwtProvider jwtProvider;

    public void joinUser(Principal principal, ChatDTO.RequestJoinDTO requestJoinDTO) {
        ChatSession chatSession = ChatSession.builder()
                .id(principal.getName()).roomId(requestJoinDTO.getRoomId()).channelId(requestJoinDTO.getChannelId()).build();
        if(requestJoinDTO.getAccessToken() != null && jwtProvider.validateToken(requestJoinDTO.getAccessToken())) {
            CustomUserDetail userDetail = (CustomUserDetail) jwtProvider.getAuthentication(requestJoinDTO.getAccessToken()).getPrincipal();
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

    public void exitUser(Principal principal) {
        ChatSession chatSession = chatSessionRedisRepository.findById(principal.getName()).orElseThrow(() -> new CustomException(CustomError.NOT_FOUND_SESSION));
        
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
            chatSessionRedisRepository.deleteById(principal.getName());
        }
    }

    private String makeRedisLockKey(String roomId) {
        final String keyPrefix = "STREAM_";
        return keyPrefix + roomId;
    }

    public ChatDTO.BroadCastMessage broadcastMessage(ChatDTO.SendMessage sendMessage, Principal principal) {
        ChatSession chatSession = getChatSession(principal);
        ChatMessage cachedMessage = cachingMessage(sendMessage, chatSession);

        ChatDTO.SenderDTO senderDTO = new ChatDTO.SenderDTO();
        senderDTO.setId(cachedMessage.getSender().getId());
        senderDTO.setNickname(cachedMessage.getSender().getNickname());

        ChatDTO.BroadCastMessage broadCastMessage = ChatDTO.BroadCastMessage.builder()
                .roomId(cachedMessage.getRoomId()).content(cachedMessage.getContent())
                .sender(senderDTO).build();

        return broadCastMessage;
    }

    private ChatSession getChatSession(Principal principal) {
        ChatSession chatSession = chatSessionRedisRepository.findById(principal.getName()).orElseThrow(() -> new CustomException(CustomError.NOT_FOUND_SESSION));
        if(chatSession.getOwnerId() == null) {
            throw new CustomException(CustomError.GUEST_SESSION);
        }
        return chatSession;
    }

    private ChatMessage cachingMessage(ChatDTO.SendMessage sendMessage, ChatSession chatSession) {
        ChatMessage.Sender sender = ChatMessage.Sender.builder()
                        .id(sendMessage.getSenderId()).nickname(chatSession.getNickname())
                        .sessionId(chatSession.getId()).build();

        ChatMessage cacheMessage = ChatMessage.builder().roomId(sendMessage.getRoomId())
                .content(sendMessage.getContent()).sender(sender)
                .sendTime(Instant.now().toEpochMilli()).blinded(false).build();

        chatMessageRedisRepository.push(sendMessage.getRoomId(), cacheMessage);

        return cacheMessage;
    }

    public List<ChatDTO.BroadCastMessage> getPrevMessages(Principal principal, Integer count) {
        ChatSession chatSession = chatSessionRedisRepository.findById(principal.getName()).orElseThrow(() -> new CustomException(CustomError.NOT_FOUND_SESSION));
        List<ChatMessage> chatMessages = chatMessageRedisRepository.get(chatSession.getRoomId(), count);

        List<ChatDTO.BroadCastMessage> broadCastMessages = chatMessages.stream().map(this::mappingMessageEntityToDTO)
                .sorted(ChatDTO.BroadCastMessage::compare).toList();

        return broadCastMessages;
    }

    private ChatDTO.BroadCastMessage mappingMessageEntityToDTO(ChatMessage entity) {
        ChatDTO.BroadCastMessage dto = new ChatDTO.BroadCastMessage();
        dto.setRoomId(entity.getRoomId());
        dto.setSender(new ChatDTO.SenderDTO(entity.getSender().getId(), entity.getSender().getNickname()));
        dto.setContent(entity.getContent());
        dto.setSendTime(entity.getSendTime());

        return dto;
    }
}
