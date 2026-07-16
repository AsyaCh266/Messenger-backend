package com.example.messenger.message.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebSocketMessageRequest{
    private Long chatId;
    private String content;
}