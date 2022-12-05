package com.sdy.bbbb.webSocket;

import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import io.sentry.protocol.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChattingController {

    private final ChatService chatService;
    private final RoomService roomService;
//    private final SimpMessagingTemplate template;


    //채팅방 생성
    @PostMapping("/room")
    public GlobalResponseDto<RoomResponseDto> createRoom(@RequestBody Long guestId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return roomService.createRoom(guestId, userDetails.getAccount());
    }

    //채팅방 입장
    @GetMapping("/room/{roomId}")
    public GlobalResponseDto<RoomResponseDto> joinRoom(@PathVariable Long roomId,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return roomService.joinRoom(roomId, userDetails.getAccount());
    }

    //채팅방 목록 조회
    @GetMapping("/myrooms")
    public GlobalResponseDto<List<RoomListResponseDto>> myRooms(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return roomService.getMyRooms(userDetails.getAccount());
    }

    //채팅
    @MessageMapping("/{roomId}")
    @SendTo("/room/{roomId}")
    //@PathVariable 과 유사
    public void createChat(@DestinationVariable Long roomId,
                     ChattingDto chattingDto,
                     @AuthenticationPrincipal UserDetailsImpl userDetails) {

        chatService.createChat(roomId, chattingDto, userDetails.getAccount());
//        template.convertAndSend("/room/" + roomId, chattingDto);
    }

}
