package com.example.messenger.chat.service.imppp;

import com.example.messenger.chat.dto.ParticipantResponse;
import com.example.messenger.chat.dto.ChatResponse;
import com.example.messenger.chat.dto.CreateGroupChat;
import com.example.messenger.chat.dto.CreatePrivateChat;
import com.example.messenger.chat.entity.Chat;
import com.example.messenger.chat.entity.ChatParticipant;
import com.example.messenger.chat.repository.ChatParticipantRepository;
import com.example.messenger.chat.repository.ChatRepository;
import com.example.messenger.chat.service.ChatService;
import com.example.messenger.user.entity.User;
import com.example.messenger.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.example.messenger.chat.dto.AddParticipantRequest;

@Service
@RequiredArgsConstructor
public class ChatServiceImppp implements ChatService{
    private final ChatRepository chatRepository;
    private final ChatParticipantRepository participantRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ChatResponse createPrivateChat(CreatePrivateChat request){
        String username = SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();
        User creator = userRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));
        User secondUser = userRepository
                        .findById(request.getUserId())
                        .orElseThrow(() -> new RuntimeException("Second user not found"));
        Chat chat = Chat.builder()
                .type("PRIVATE")
                .createdAt(LocalDateTime.now())
                .creator(creator)
                .build();

        chat = chatRepository.save(chat);
        ChatParticipant first = ChatParticipant.builder()
                        .chat(chat)
                        .user(creator)
                        .role("ADMIN")
                        .joinedAt(LocalDateTime.now())
                        .build();
        ChatParticipant second = ChatParticipant.builder()
                        .chat(chat)
                        .user(secondUser)
                        .role("MEMBER")
                        .joinedAt(LocalDateTime.now())
                        .build();

        participantRepository.save(first);
        participantRepository.save(second);
        return ChatResponse.builder()
                .id(chat.getId())
                .name("Private Chat")
                .type(chat.getType())
                .build();
    }

    @Override
    @Transactional
    public ChatResponse createGroupChat(CreateGroupChat request){
        String username = SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();
        User creator = userRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));
        Chat chat = Chat.builder()
                .type("GROUP")
                .name(request.getName())
                .createdAt(LocalDateTime.now())
                .creator(creator)
                .build();
        chat = chatRepository.save(chat);
        ChatParticipant admin = ChatParticipant.builder()
                        .chat(chat)
                        .user(creator)
                        .role("ADMIN")
                        .joinedAt(LocalDateTime.now())
                        .build();

        participantRepository.save(admin);
        for (Long userId : request.getParticipantIds()){
            User user = userRepository
                            .findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));
            ChatParticipant participant = ChatParticipant.builder()
                            .chat(chat)
                            .user(user)
                            .role("MEMBER")
                            .joinedAt(LocalDateTime.now())
                            .build();
            participantRepository.save(participant);
        }

        return ChatResponse.builder()
                .id(chat.getId())
                .name(chat.getName())
                .type(chat.getType())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatResponse> getMyChats(){
        String username = SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();
        User currentUser = userRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));
        List<ChatParticipant> participants = participantRepository
                        .findByUserId(currentUser.getId());

        return participants.stream()
                .map(ChatParticipant::getChat)
                .map(chat -> ChatResponse.builder()
                                .id(chat.getId())
                                .name(chat.getName())
                                .type(chat.getType())
                                .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipantResponse>
    getChatParticipants(Long chatId){
        List<ChatParticipant> participants = participantRepository.findByChatId(chatId);
        return participants.stream()
                .map(participant -> ParticipantResponse.builder()
                                .id(participant.getUser().getId()).username(participant.getUser().getUsername())
                                .role(participant.getRole())
                                .build())
                .toList();
    }

    @Override
    @Transactional
    public void addParticipant(Long chatId, AddParticipantRequest request){
        String username = SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User currentUser = userRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));
        ChatParticipant me = participantRepository
                        .findByChatIdAndUserId(chatId, currentUser.getId())
                        .orElseThrow(() -> new RuntimeException("You are not in this chat"));

        if (!me.getRole().equals("ADMIN")){
            throw new RuntimeException("Only admin can add participants");
        }
        if (participantRepository.existsByChatIdAndUserId(chatId, request.getUserId())){
            throw new RuntimeException("User already in chat");
        }

        User newUser = userRepository
                        .findById(request.getUserId())
                        .orElseThrow(() -> new RuntimeException("User not found"));

        Chat chat = me.getChat();
        ChatParticipant participant = ChatParticipant.builder()
                        .chat(chat)
                        .user(newUser)
                        .role("MEMBER")
                        .joinedAt(LocalDateTime.now())
                        .build();
        participantRepository.save(participant);
    }

    @Override
    @Transactional
    public void removeParticipant(Long chatId, Long userId){
        String username = SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();
        User currentUser = userRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));
        ChatParticipant me = participantRepository
                        .findByChatIdAndUserId(chatId, currentUser.getId())
                        .orElseThrow(() -> new RuntimeException("You are not in this chat"));

        if (!me.getRole().equals("ADMIN")){
            throw new RuntimeException(
                    "Only admin can remove participants"
            );
        }

        if (!participantRepository.existsByChatIdAndUserId(chatId, userId)){
            throw new RuntimeException("User not in chat");
        }

        participantRepository.deleteByChatIdAndUserId(chatId, userId);
    }
}