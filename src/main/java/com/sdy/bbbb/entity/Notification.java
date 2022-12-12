package com.sdy.bbbb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sdy.bbbb.util.AlarmType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Notification extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Boolean isRead;

    @Column(nullable = false)
    private String data;

    @ManyToOne
    @JsonBackReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "receiver_member_id")
    private Account account;


    @Builder
    public Notification(AlarmType alarmType, String message, Boolean readState,
                        String data, Account receiver) {
        this.alarmType = alarmType;
        this.message = message;
        this.isRead = readState;
        this.data = data;
        this.account = receiver;
    }

    public void changeState() {
        isRead = true;
    }

}
