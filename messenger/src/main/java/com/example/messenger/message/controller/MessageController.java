package com.example.messenger.message.controller;

import com.example.messenger.message.dto.MessageResponse;
import com.example.messenger.message.dto.SendMessage;
import com.example.messenger.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.messenger.message.dto.EditMessage;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class MessageController{
    private final MessageService messageService;

    @PostMapping("/{chatId}/messages")
    public MessageResponse sendMessage(@PathVariable Long chatId, @RequestBody SendMessage request){
        return messageService.sendMessage(chatId, request);
    }

    @GetMapping("/{chatId}/messages")
    public List<MessageResponse> getMessages(@PathVariable Long chatId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size){
        return messageService.getMessages(chatId, page, size);
    }

    @DeleteMapping("/messages/{messageId}")
    public void deleteMessage(@PathVariable Long messageId){
        messageService.deleteMessage(messageId);
    }

    @PatchMapping("/messages/{messageId}")
    public MessageResponse editMessage(@PathVariable Long messageId, @RequestBody EditMessage request){
        return messageService.editMessage(messageId, request);
    }

    @GetMapping("/{chatId}/messages/search")
    public List<MessageResponse> searchMessages(@PathVariable Long chatId, @RequestParam String text){
        return messageService.searchMessages(chatId, text);
    }
}