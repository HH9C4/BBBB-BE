package com.sdy.bbbb.repository;

import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

//    @Query(value = "SELECT r from Room r join fetch r.chatList where r.host = ?1 ")
//    List<Room> findRoomsByHostOrGuest(Account account, Account account);

    Optional<Room> findByGuestAndHost(Account account1, Account account2);

    @Query(value = "Select distinct r From Room r left join fetch r.chatList where r.id = ?1")
    Optional<Room> findByIdFecthChatList(Long roomId);

    @Query(value = "SELECT r FROM Room r join fetch r.host join fetch r.guest where r.id = ?1")
    Optional<Room> findByIdFetchHostAndGuest(Long roomId);

    @Query(value = "SELECT distinct r from Room r join fetch r.guest join fetch r.host join fetch r.chatList where r.host.id = ?1 or r.guest.id = ?2")
    List<Room> findRoomsByHostOrGuest(Long account, Long account1);
    
}
