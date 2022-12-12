package com.sdy.bbbb.dto.response;

import com.sdy.bbbb.entity.Chat;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
