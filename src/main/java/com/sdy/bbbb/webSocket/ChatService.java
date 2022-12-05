package com.sdy.bbbb.webSocket;

import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public void createChat(Long roomId, ChattingDto chattingDto, Account sender) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new CustomException(ErrorCode.NotFoundComment));
        Chat chat = new Chat(room, sender, chattingDto.getMessage());
        chatRepository.save(chat);
    }
}
