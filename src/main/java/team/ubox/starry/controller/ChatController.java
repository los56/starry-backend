package team.ubox.starry.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import team.ubox.starry.service.dto.chat.ChatDTO;

import java.sql.Timestamp;
import java.time.Instant;

@Controller
@RequestMapping("/api/chat")
public class ChatController {

    @MessageMapping("/send/{channelId}")
    @SendTo("/subscribe/{channelId}")
    public ChatDTO.BroadCastMessage broadcast( ChatDTO.SendMessage message) {
        return new ChatDTO.BroadCastMessage(message.getSender(), message.getContent(), Timestamp.from(Instant.now()));
    }
}
