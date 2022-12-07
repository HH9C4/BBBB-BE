package com.sdy.bbbb.webSocket;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.TimeStamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Chat extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    @JsonBackReference
    private Room room;

    @ManyToOne
    @JoinColumn
    private Account account;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column
    private Boolean isLast;

    public Chat(Room room, Account sender, String message) {
        this.room = room;
        this.account = sender;
        this.message = message;
        this.modifiedAt = LocalDateTime.now();
        this.isLast = false;
    }
}
