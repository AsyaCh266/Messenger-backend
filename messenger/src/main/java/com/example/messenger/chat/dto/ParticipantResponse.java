package com.example.messenger.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ParticipantResponse{
    private Long id;
    private String username;
    private String role;
}