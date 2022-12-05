package com.sdy.bbbb.webSocket;

import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import com.sdy.bbbb.repository.AccountRepository;
import com.sdy.bbbb.util.Chrono;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final AccountRepository accountRepository;

    public GlobalResponseDto<RoomResponseDto> createRoom(String guestNickName, Account host) {
        Account guest = accountRepository.findAccountByAccountName(guestNickName).orElseThrow(
                ()-> new CustomException(ErrorCode.NotFoundUser));

        Optional<Room> room1 = roomRepository.findByGuestAndHost(guest, host);
        Optional<Room> room2 = roomRepository.findByGuestAndHost(host, guest);

        if (room1.isEmpty() && room2.isEmpty()){
            List<Account> participants = new ArrayList<>();
            participants.add(guest);
            participants.add(host);
            StringBuilder roomName = new StringBuilder();
            for(int i = 0; i < participants.size(); i ++){
                roomName.append(participants.get(i).getAccountName());
                if(i != participants.size() - 1) {
                    roomName.append(", ");
                }
            }
            Room room = new Room(roomName.toString(), participants.size(), host, guest);
            roomRepository.save(room);
            return GlobalResponseDto.ok("success create room", new RoomResponseDto(room));
        } else {
            Room room = room1.orElseGet(room2::get);
            return GlobalResponseDto.ok("이미 채팅방이 존재합니다.", new RoomResponseDto(room));
        }

    }

    // 채팅방 입장(원래 있던 채팅내역 보내주는 것)
    public GlobalResponseDto<RoomResponseDto> joinRoom(Long roomId, Account account) {
//        Room room = roomRepository.findByIdFecthChatList(roomId).orElseThrow(
//                ()-> new CustomException(ErrorCode.NotFoundRoom));
        Room room = roomRepository.findByIdFecthChatList1(roomId).get(0);
        return GlobalResponseDto.ok("success", new RoomResponseDto(room));
    }

    // 룸 리스트 리턴 (내가 속해있는 채팅방 목록)
    public GlobalResponseDto<List<RoomListResponseDto>> getMyRooms(Account account) {
        List<Room> roomList = roomRepository.findRoomsByHostOrGuest(account, account);
        List<RoomListResponseDto> rldList = new ArrayList<>();
        for(Room room : roomList){
            Account other = room.getHost().getId().equals(account.getId()) ? room.getGuest() : room.getHost();

            if(room.getChatList().size()>0) {
                Chat lastChat = room.getChatList().get(room.getChatList().size() - 1);
                String lastMessage = lastChat.getMessage();
                String lastMessageTime = Chrono.timesAgo(lastChat.getCreatedAt());
                rldList.add(new RoomListResponseDto(room.getRoomName(), other.getProfileImage(), lastMessage, lastMessageTime));
            }else {
                rldList.add(new RoomListResponseDto(room.getRoomName(), other.getProfileImage(), null, null));
            }
        }

        return GlobalResponseDto.ok("조회완료", rldList);
    }

    // 방 나가기

}
