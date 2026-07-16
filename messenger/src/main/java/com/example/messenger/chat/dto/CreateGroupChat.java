package com.example.messenger.chat.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CreateGroupChat{
    private String name;
    private List<Long> participantIds;
}