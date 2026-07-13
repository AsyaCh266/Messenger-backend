package com.example.messenger.message.repository;

import com.example.messenger.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>{
    List<Message> findByChat_Id(Long chatId);
}