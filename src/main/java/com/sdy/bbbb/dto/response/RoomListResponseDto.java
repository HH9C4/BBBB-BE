package com.sdy.bbbb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomListResponseDto {

    private Long roomId;

    private String roomName;

    private String otherProfilePic;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastMessage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastMessageTime;

    public RoomListResponseDto(Long roomId, String roomName, String otherProfilePic, String lastMessage, String lastMessageTime) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.otherProfilePic = otherProfilePic;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }

}
