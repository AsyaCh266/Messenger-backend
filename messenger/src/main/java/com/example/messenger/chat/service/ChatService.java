package com.example.messenger.chat.service;

import com.example.messenger.chat.dto.ChatResponse;
import com.example.messenger.chat.dto.CreateGroupChat;
import com.example.messenger.chat.dto.CreatePrivateChat;
import com.example.messenger.chat.dto.ParticipantResponse;
import com.example.messenger.chat.dto.AddParticipantRequest;
import java.util.List;

public interface ChatService{
    ChatResponse createPrivateChat(CreatePrivateChat request);
    ChatResponse createGroupChat(CreateGroupChat request);
    List<ChatResponse> getMyChats();
    List<ParticipantResponse> getChatParticipants(Long chatId);
    void addParticipant(Long chatId, AddParticipantRequest request);
    void removeParticipant(Long chatId, Long userId);
}