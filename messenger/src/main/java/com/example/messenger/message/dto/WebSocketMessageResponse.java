package com.example.messenger.message.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class WebSocketMessageResponse{
    private Long messageId;
    private Long chatId;
    private Long senderId;
    private String senderUsername;
    private String content;
    private LocalDateTime createdAt;
}