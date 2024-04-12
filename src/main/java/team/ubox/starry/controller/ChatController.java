package team.ubox.starry.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import team.ubox.starry.exception.CustomError;
import team.ubox.starry.exception.CustomException;
import team.ubox.starry.helper.UUIDHelper;
import team.ubox.starry.repository.entity.redis.ChatSession;
import team.ubox.starry.repository.redis.ChatSessionRedisRepository;
import team.ubox.starry.service.ChatService;
import team.ubox.starry.service.dto.CustomResponse;
import team.ubox.starry.service.dto.chat.ChatDTO;

import java.sql.Timestamp;
import java.time.Instant;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ChatService chatService;
    private final ChatSessionRedisRepository chatSessionRedisRepository;

    @MessageMapping("/")
    public void broadcast(@Payload ChatDTO.SendMessage message, @Header("simpSessionId") String sessionId) {
        ChatSession chatSession = chatSessionRedisRepository.findById(sessionId).orElseThrow(() -> new CustomException(CustomError.NOT_FOUND_SESSION));
        if(chatSession.getOwnerId() == null) {
            throw new CustomException(CustomError.GUEST_SESSION);
        }

        ChatDTO.SenderDTO sender = new ChatDTO.SenderDTO();
        sender.setId(UUIDHelper.UUIDToString(chatSession.getOwnerId()));
        sender.setNickname(chatSession.getNickname());

        ChatDTO.BroadCastMessage broadCastMessage = new ChatDTO.BroadCastMessage();
        broadCastMessage.setSender(sender);
        broadCastMessage.setContent(message.getContent());
        broadCastMessage.setSendTime(Instant.now().toEpochMilli());

        simpMessageSendingOperations.convertAndSend("/subscribe/" + message.getRoomId(), broadCastMessage);
    }

}
