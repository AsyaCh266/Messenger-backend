package com.example.messenger.message.repository;

import com.example.messenger.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageRepository extends JpaRepository<Message, Long>{
    Page<Message> findByChat_Id(Long chatId, Pageable pageable);
    List<Message> findByChat_IdAndContentContainingIgnoreCase(Long chatId, String text);
}