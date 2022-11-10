package com.sdy.bbbb.entity;

import io.swagger.annotations.ApiModelProperty;
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
    private Integer liketLevel;
    @ManyToOne
    @JoinColumn
    private Account account;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public Like(Post post, Integer liketLevel, Account account){
        this.post = post;
        this.account = account;
        this.liketLevel = liketLevel;
    }
    public Like(Comment comment, Integer liketLevel, Account account){
        this.comment = comment;
        this.account = account;
        this.liketLevel = liketLevel;
    }


}
