package com.example.messenger.chat.entity;

import com.example.messenger.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String name;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User creator;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}