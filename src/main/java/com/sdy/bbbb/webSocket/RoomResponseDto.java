package com.sdy.bbbb.webSocket;

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

    private List<Chat> chatList;

    public RoomResponseDto(Room room) {
        this.roomId = room.getId();
        this.roomName = room.getRoomName();
        this.hostName = room.getHost().getAccountName();
        this.guestName = room.getGuest().getAccountName();
        this.chatList = room.getChatList();
    }
}
