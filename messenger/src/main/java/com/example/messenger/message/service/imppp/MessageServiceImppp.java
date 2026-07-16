package com.example.messenger.message.service.imppp;

import com.example.messenger.chat.entity.Chat;
import com.example.messenger.chat.repository.ChatParticipantRepository;
import com.example.messenger.chat.repository.ChatRepository;
import com.example.messenger.message.dto.MessageResponse;
import com.example.messenger.message.dto.SendMessage;
import com.example.messenger.message.entity.Message;
import com.example.messenger.message.repository.MessageRepository;
import com.example.messenger.message.service.MessageService;
import com.example.messenger.user.entity.User;
import com.example.messenger.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import com.example.messenger.message.dto.SendMessage;
import com.example.messenger.message.dto.EditMessage;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.example.messenger.message.dto.WebSocketMessageRequest;
import com.example.messenger.message.dto.WebSocketMessageResponse;

@Service
@RequiredArgsConstructor
public class MessageServiceImppp implements MessageService{
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatParticipantRepository participantRepository;

    @Override
    @Transactional
    public MessageResponse sendMessage(Long chatId, SendMessage request){
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        User sender = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Chat chat = chatRepository
                .findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
        boolean isParticipant = participantRepository.existsByChatIdAndUserId(chatId, sender.getId());

        if (!isParticipant){
            throw new RuntimeException("You are not a participant of this chat");
        }

        Message message = Message.builder()
                .chat(chat)
                .sender(sender)
                .content(request.getContent())
                .messageType("TEXT")
                .createdAt(LocalDateTime.now())
                .deleted(false)
                .build();

        message = messageRepository.save(message);
        return MessageResponse.builder()
                .id(message.getId())
                .senderId(sender.getId())
                .senderUsername(sender.getUsername())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageResponse> getMessages(Long chatId, int page, int size){
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        User currentUser = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        boolean isParticipant = participantRepository.existsByChatIdAndUserId(chatId, currentUser.getId());

        if (!isParticipant){
            throw new RuntimeException("You are not a participant of this chat");
        }

        Pageable pageable = PageRequest.of(page, size);
        return messageRepository
                .findByChat_Id(chatId, pageable)
                .stream()
                .filter(message -> !Boolean.TRUE.equals(message.getDeleted()))
                .map(message -> MessageResponse.builder()
                                .id(message.getId())
                                .senderId(message.getSender().getId())
                                .senderUsername(message.getSender().getUsername())
                                .content(message.getContent())
                                .createdAt(message.getCreatedAt())
                                .build())
                .toList();
    }

    @Override
    @Transactional
    public void deleteMessage(Long messageId){
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        User currentUser = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Message message = messageRepository
                .findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!message.getSender().getId().equals(currentUser.getId())){
            throw new RuntimeException("You can delete only your messages");
        }

        message.setDeleted(true);
        messageRepository.save(message);
    }

    @Override
    @Transactional
    public MessageResponse editMessage(Long messageId, EditMessage request){
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        User currentUser = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Message message = messageRepository
                .findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!message.getSender().getId().equals(currentUser.getId())){
            throw new RuntimeException("You can edit only your messages");
        }

        message.setContent(request.getContent());
        message.setEditedAt(LocalDateTime.now());
        messageRepository.save(message);
        return MessageResponse.builder()
                .id(message.getId())
                .senderId(currentUser.getId())
                .senderUsername(currentUser.getUsername())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageResponse> searchMessages(Long chatId, String text){
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        User currentUser = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        boolean isParticipant = participantRepository.existsByChatIdAndUserId(chatId, currentUser.getId());

        if (!isParticipant){
            throw new RuntimeException("You are not a participant of this chat");
        }

        return messageRepository
                .findByChat_IdAndContentContainingIgnoreCase(chatId, text)
                .stream()
                .filter(message -> !Boolean.TRUE.equals(message.getDeleted()))
                .map(message -> MessageResponse.builder()
                                .id(message.getId())
                                .senderId(message.getSender().getId())
                                .senderUsername(message.getSender().getUsername())
                                .content(message.getContent())
                                .createdAt(message.getCreatedAt())
                                .build())
                .toList();
    }

    @Override
    @Transactional
    public WebSocketMessageResponse sendWebSocketMessage(WebSocketMessageRequest request){
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        User sender = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Chat chat = chatRepository
                .findById(request.getChatId())
                .orElseThrow(() -> new RuntimeException("Chat not found"));
        boolean isParticipant = participantRepository
                        .existsByChatIdAndUserId(request.getChatId(), sender.getId());

        if (!isParticipant){
            throw new RuntimeException("You are not participant of this chat");
        }

        Message message = Message.builder()
                .chat(chat)
                .sender(sender)
                .content(request.getContent())
                .messageType("TEXT")
                .createdAt(LocalDateTime.now())
                .deleted(false)
                .build();

        message = messageRepository.save(message);
        return WebSocketMessageResponse.builder()
                .messageId(message.getId())
                .chatId(chat.getId())
                .senderId(sender.getId())
                .senderUsername(sender.getUsername())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .build();
    }
}