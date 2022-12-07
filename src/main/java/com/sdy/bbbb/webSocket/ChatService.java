package com.sdy.bbbb.webSocket;

import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import com.sdy.bbbb.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final RoomRepository roomRepository;
    private  final AccountRepository accountRepository;

    @Transactional
    public void createChat(Long roomId, ChattingDto chattingDto) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new CustomException(ErrorCode.NotFoundRoom));
        Account account = accountRepository.findAccountByAccountName(chattingDto.getSender()).orElseThrow(()-> new CustomException(ErrorCode.NotFoundUser));
        if(room.isHostLeave() || room.isGuestLeave()) {
            room.setGuestLeave(false);
            room.setHostLeave(false);
        }
        Chat chat = new Chat(room, account, chattingDto.getMessage());
        chatRepository.save(chat);
    }
}
