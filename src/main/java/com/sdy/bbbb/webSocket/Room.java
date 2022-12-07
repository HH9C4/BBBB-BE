package com.sdy.bbbb.webSocket;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.TimeStamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Room extends TimeStamped {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Integer participantsNum;

    @JsonManagedReference
    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE)
    private List<Chat> chatList = new ArrayList<>();

    public Room(String roomName, Integer participantSize, Account host, Account guest) {
        this.roomName = roomName;
        this.participantsNum = participantSize;
        this.host = host;
        this.guest = guest;
    }

}
