package com.sdy.bbbb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdy.bbbb.dto.request.CommentRequestDto;
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
public class Comment extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @Column
    private boolean isChecked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Post post;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Like> likeList = new ArrayList<>();

    @Column(nullable = false)
    private int likeCount;


    public Comment(CommentRequestDto requestDto, Post post, Account account) {
        this.account = account;
        this.post = post;
        this.comment = requestDto.getComment();
    }
}
