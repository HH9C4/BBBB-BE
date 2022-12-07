package com.sdy.bbbb.webSocket;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.TimeStamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Room extends TimeStamped {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Integer participantsNum;

    @JsonManagedReference
    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE)
    private List<Chat> chatList = new ArrayList<>();

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Account host;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Account guest;

    private boolean HostLeave;

    private boolean GuestLeave;


    public Room(Integer participantsNum, Account host, Account guest){
        this.participantsNum = participantsNum;
        this.host = host;
        this.guest = guest;
        this.HostLeave = false;
        this.GuestLeave = false;
    }
}
