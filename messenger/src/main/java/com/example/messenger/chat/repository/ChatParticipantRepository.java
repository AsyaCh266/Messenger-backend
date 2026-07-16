package com.example.messenger.chat.repository;

import com.example.messenger.chat.entity.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long>{
    List<ChatParticipant> findByUserId(Long userId);
    List<ChatParticipant> findByChatId(Long chatId);
    boolean existsByChatIdAndUserId(Long chatId, Long userId);
    Optional<ChatParticipant> findByChatIdAndUserId(Long chatId, Long userId);
    void deleteByChatIdAndUserId(Long chatId, Long userId);
}