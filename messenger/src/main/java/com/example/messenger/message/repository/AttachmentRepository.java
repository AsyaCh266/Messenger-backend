package com.example.messenger.message.repository;

import com.example.messenger.message.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long>{
    List<Attachment> findByMessage_Id(Long messageId);
}