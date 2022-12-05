package com.sdy.bbbb.webSocket;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RoomResponseDto {

    private Long roomId;

    private String roomName;

    private String hostName;

    private String guestName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ChatResponseDto> chatList;

    public RoomResponseDto(Room room, List<ChatResponseDto> chatResponseDto) {
        this.roomId = room.getId();
        this.roomName = room.getRoomName();
        this.hostName = room.getHost().getAccountName();
        this.guestName = room.getGuest().getAccountName();
        this.chatList = chatResponseDto;
    }
}
