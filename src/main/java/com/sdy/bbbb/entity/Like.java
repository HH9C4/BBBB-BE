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

    @Column(nullable = false)
    private Integer likeLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Comment comment;

    public Like(Post post, Integer likeLevel, Account account){
        this.post = post;
        this.account = account;
        this.likeLevel = likeLevel;
    }

    public Like(Comment comment, Integer likeLevel, Account account){
        this.comment = comment;
        this.account = account;
        this.likeLevel = likeLevel;
    }

}
