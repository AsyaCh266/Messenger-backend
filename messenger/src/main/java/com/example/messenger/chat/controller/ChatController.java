package com.example.messenger.chat.controller;

import com.example.messenger.chat.dto.ParticipantResponse;
import com.example.messenger.chat.dto.ChatResponse;
import com.example.messenger.chat.dto.CreateGroupChat;
import com.example.messenger.chat.dto.CreatePrivateChat;
import com.example.messenger.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.messenger.chat.dto.AddParticipantRequest;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController{
    private final ChatService chatService;

    @PostMapping("/private")
    public ChatResponse createPrivateChat(@RequestBody CreatePrivateChat request){
        return chatService.createPrivateChat(request);
    }

    @PostMapping("/group")
    public ChatResponse createGroupChat(@RequestBody CreateGroupChat request){
        return chatService.createGroupChat(request);
    }

    @GetMapping
    public List<ChatResponse> getMyChats(){
        return chatService.getMyChats();
    }

    @GetMapping("/{chatId}/participants")
    public List<ParticipantResponse>
    getParticipants(@PathVariable Long chatId){
        return chatService.getChatParticipants(chatId);
    }

    @PostMapping("/{chatId}/participants")
    public void addParticipant(@PathVariable Long chatId, @RequestBody AddParticipantRequest request){
        chatService.addParticipant(chatId, request);
    }

    @DeleteMapping("/{chatId}/participants/{userId}")
    public void removeParticipant(@PathVariable Long chatId, @PathVariable Long userId){
        chatService.removeParticipant(chatId, userId);
    }
}