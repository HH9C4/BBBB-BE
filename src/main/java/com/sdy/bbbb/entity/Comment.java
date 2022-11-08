package com.sdy.bbbb.entity;

import com.sdy.bbbb.dto.request.CommentRequestDto;
import com.sdy.bbbb.dto.response.CommentResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Comment extends TimeStamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    private String commentLevel;
    private LocalDateTime time = LocalDateTime.now();
    private boolean isChecked;
    @ManyToOne
    @JoinColumn
    private Account account;
    @ManyToOne
    @JoinColumn
    private Post post;


    public Comment(CommentRequestDto requestDto, Post post, Account account) {
        this.account = account;
        this.post = post;
        this.comment = requestDto.getComment();
        this.commentLevel = requestDto.getCommentLevel();
    }
    public CommentResponseDto responseDto(){
        return new CommentResponseDto(this.id, this.account.getAccountName(), this.comment, this.time);
    }
}
