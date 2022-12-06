package com.sdy.bbbb.webSocket;

import com.sdy.bbbb.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findRoomsByHostOrGuest(Account account, Account account1);


//    @Query(value = "Select r from Room r join Account a on r.participants.")
//    Optional<Room> findByParticipants(List<Account> participants);

    Optional<Room> findByGuestAndHost(Account account1, Account account2);


//    @Query(value = "Select distinct r From Room r left join fetch r.chatList")
//    Optional<Room> findByIdFecthChatList(Long roomId);

    @Query(value = "Select distinct r From Room r left join fetch r.chatList where r.id = ?1")
    Optional<Room> findByIdFecthChatList(Long roomId);
}
