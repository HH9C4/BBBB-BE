package com.sdy.bbbb.controller;

import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.request.ChattingDto;
import com.sdy.bbbb.dto.request.RoomRequestDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.dto.response.RoomListResponseDto;
import com.sdy.bbbb.dto.response.RoomResponseDto;
import com.sdy.bbbb.service.ChatService;
import com.sdy.bbbb.service.RoomService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChattingController {

    private final ChatService chatService;
    private final RoomService roomService;
    private final SimpMessagingTemplate template;


    //채팅방 생성
    @ApiOperation(value = "채팅방 생성", notes = "채팅방 생성")
    @PostMapping("/room")
    public GlobalResponseDto<RoomResponseDto> createRoom(@RequestBody RoomRequestDto roomRequestDto,
                                                         @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return roomService.createRoom(roomRequestDto.getOtherUserNickname(), userDetails.getAccount());
    }

    //채팅방 입장
    @ApiOperation(value = "채팅방 입장", notes = "채팅방 입장")
    @GetMapping("/room/{roomId}")
    public GlobalResponseDto<RoomResponseDto> joinRoom(@PathVariable Long roomId,
                                                       @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return roomService.joinRoom(roomId, userDetails.getAccount());
    }

    //채팅방 나가기
    @ApiOperation(value = "채팅방 나가기", notes = "채팅방 나가기")
    @PutMapping("/room/{roomId}")
    public GlobalResponseDto<?> leaveRoom(@PathVariable Long roomId,
                                          @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return roomService.leaveRoom(roomId, userDetails.getAccount());
    }

    //채팅방 목록 조회
    @ApiOperation(value = "채팅방 목록 조회", notes = "채팅방 목록 조회")
    @GetMapping("/myrooms")
    public GlobalResponseDto<List<RoomListResponseDto>> myRooms(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return roomService.getMyRooms(userDetails.getAccount());
    }

    //채팅
    @ApiOperation(value = "채팅", notes = "체팅 보내기")
    @MessageMapping("/{roomId}")
    @SendTo("/sub/{roomId}")
    //@PathVariable 과 유사
    public void createChat(@DestinationVariable Long roomId,
                     ChattingDto chattingDto) {

        chatService.createChat(roomId, chattingDto);
        template.convertAndSend("/sub/" + roomId, chattingDto);
    }

}
