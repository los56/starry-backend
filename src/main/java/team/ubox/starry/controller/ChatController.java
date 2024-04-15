package team.ubox.starry.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
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

import java.security.Principal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ChatService chatService;
    private final ChatSessionRedisRepository chatSessionRedisRepository;

    @MessageMapping("/")
    public void broadcast(@Payload ChatDTO.SendMessage message, Principal principal) {
        ChatDTO.BroadCastMessage broadCastMessage = chatService.broadcastMessage(message, principal);

        simpMessageSendingOperations.convertAndSend("/subscribe/" + broadCastMessage.getRoomId(), broadCastMessage);
    }

    @MessageMapping("/prev")
    public void getPrevMessages(@Payload PrevCount prevCount, Principal principal) {
        Integer count = prevCount.count;
        if(count == null || count < 1) {
            count = 50;
        }

        List<ChatDTO.BroadCastMessage> broadCastMessages = chatService.getPrevMessages(principal, count);

        simpMessageSendingOperations.convertAndSendToUser(principal.getName(), "/prev", broadCastMessages);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PrevCount {
        private Integer count;
    }
}
