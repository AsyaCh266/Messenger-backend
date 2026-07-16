package com.example.messenger.message.service;

import com.example.messenger.message.dto.MessageResponse;
import com.example.messenger.message.dto.SendMessage;
import java.util.List;
import com.example.messenger.message.dto.EditMessage;

public interface MessageService{
    MessageResponse sendMessage(Long chatId, SendMessage request);
    List<MessageResponse> getMessages(Long chatId, int page, int size);
    void deleteMessage(Long messageId);
    MessageResponse editMessage(Long messageId, EditMessage request);
    List<MessageResponse> searchMessages(Long chatId, String text);
}