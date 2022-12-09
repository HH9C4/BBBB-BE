package com.sdy.bbbb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sdy.bbbb.dto.response.ChatResponseDto;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RoomResponseDto {

    private Long roomId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String roomName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String myName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String yourName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String guestProfile;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ChatResponseDto> chatList;

    public RoomResponseDto(Room room) {
        this.roomId = room.getId();
    }

    public RoomResponseDto(Room room, Account me, Account you, List<ChatResponseDto> chatResponseDto) {
        this.roomId = room.getId();
        this.roomName = you.getAccountName();
        this.myName = me.getAccountName();
        this.yourName = you.getAccountName();
        this.guestProfile = you.getProfileImage();
        this.chatList = chatResponseDto;
    }

//    public RoomResponseDto(Room room, List<ChatResponseDto> chatResponseDto) {
//        this.roomId = room.getId();
//        this.hostName = room.getHost().getAccountName();
//        this.guestName = room.getGuest().getAccountName();
//        this.guestProfile = room.getGuest().getProfileImage();
//        this.chatList = chatResponseDto;
//    }

}
