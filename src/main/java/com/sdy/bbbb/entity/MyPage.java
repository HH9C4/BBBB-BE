package com.sdy.bbbb.entity;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(hidden = true)
    private Account account;



    public MyPage(Account kakaoUser) {
        this.account = kakaoUser;
    }

}
