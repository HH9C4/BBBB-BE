package com.sdy.bbbb.webSocket;

import com.sdy.bbbb.SSE.AlarmType;
import com.sdy.bbbb.SSE.SseService2;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import com.sdy.bbbb.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final RoomRepository roomRepository;
    private final AccountRepository accountRepository;
    private final SseService2 sseService;

    @Transactional
    public void createChat(Long roomId, ChattingDto chattingDto) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new CustomException(ErrorCode.NotFoundRoom));
        Account account = accountRepository.findAccountByAccountName(chattingDto.getSender()).orElseThrow(()-> new CustomException(ErrorCode.NotFoundUser));
        if(room.isHostLeave() || room.isGuestLeave()) {
            room.setGuestLeave(false);
            room.setHostLeave(false);
        }

        Boolean amIGuest = amIGuest(room, account);
        Account other = amIGuest ? room.getHost() : room.getGuest();

        Chat chat = new Chat(room, account, chattingDto.getMessage());

        if(!Objects.equals(other.getId(), account.getId())) {
            sseService.send(other, AlarmType.eventNewChat, account.getAccountName() + "님이 " + other.getAccountName() + "님에게 새로운 채팅을 보냈습니다.", "roomId: " + room.getId());
        }
        chatRepository.save(chat);
    }

    private Boolean amIGuest(Room room, Account me) {
        return room.getGuest().getId().equals(me.getId());
    }
}
