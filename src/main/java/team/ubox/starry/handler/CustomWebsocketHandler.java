package team.ubox.starry.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import team.ubox.starry.service.ChatService;
import team.ubox.starry.service.dto.chat.ChatDTO;

import java.lang.reflect.Type;
import java.security.Principal;

@Component
@RequiredArgsConstructor
public class CustomWebsocketHandler implements ChannelInterceptor{
    private final ChatService chatService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        Principal principal = accessor.getUser();
        if(principal == null) {
            return null;
        }

        if(accessor.getCommand() == StompCommand.CONNECT) {
            String channelId = accessor.getFirstNativeHeader("channelId");
            String roomId = accessor.getFirstNativeHeader("roomId");
            String accessToken = accessor.getFirstNativeHeader("accessToken");

            chatService.joinUser(principal, new ChatDTO.RequestJoinDTO(channelId, roomId, accessToken));
        } else if(accessor.getCommand() == StompCommand.DISCONNECT) {
            String receipt = accessor.getFirstNativeHeader("receipt");
            if(receipt == null) {
                chatService.exitUser(principal);
            }
        }
        return message;
    }
}
