package com.sdy.bbbb.webSocket;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sdy.bbbb.entity.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatResponseDto {

    private Long id;
    private Long roomId;
    private String sender;
    private String message;
    private Boolean isLast;

    public ChatResponseDto(Chat chat) {
        this.id = chat.getId();
        this.roomId = chat.getRoom().getId();
        this.sender = chat.getAccount().getAccountName();
        this.message = chat.getMessage();
        this.isLast = chat.getIsLast();
    }
}
