package com.sdy.bbbb.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "likes")
@NoArgsConstructor
@Getter
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn
    private Account account;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Like(Post post, Account account){
        this.post = post;
        this.account = account;
    }


}
