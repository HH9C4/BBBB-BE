package com.sdy.bbbb.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class MyPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "accountId")
    private Account account;

//    @OneToMany
//    @JoinColumn(name = "gu")
//    private Gu gu;

    public MyPage(Account kakaoUser) {
        this.account = kakaoUser;
    }

}
