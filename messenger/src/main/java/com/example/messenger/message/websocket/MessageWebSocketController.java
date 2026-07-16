package com.example.messenger.message.websocket;

import com.example.messenger.message.dto.WebSocketMessageRequest;
import com.example.messenger.message.dto.WebSocketMessageResponse;
import com.example.messenger.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageWebSocketController{
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;


    @MessageMapping("/chat")
    public void SendMessage(WebSocketMessageRequest request) {
        System.out.println("WEBSOCKET MESSAGE RECEIVED");
        System.out.println(request.getContent());
        WebSocketMessageResponse response = messageService.sendWebSocketMessage(request);
        messagingTemplate.convertAndSend(
                "/topic/chat/" + request.getChatId(),
                response
        );
    }
}