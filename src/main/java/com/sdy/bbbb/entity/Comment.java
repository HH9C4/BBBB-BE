package com.sdy.bbbb.entity;

import com.sdy.bbbb.dto.request.CommentRequestDto;
import com.sdy.bbbb.dto.response.CommentResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    private String accountName;
    private LocalDateTime time = LocalDateTime.now();
    @ManyToOne
    @JoinColumn
    private Account account;
    @ManyToOne
    @JoinColumn
    private Post post;


    public Comment(CommentRequestDto requestDto, Post post, Account account) {
        this.account = account;
        this.post = post;
        this.accountName=account.getUsername();
        this.comment = requestDto.getComment();
    }
    public CommentResponseDto responseDto(){
        return new CommentResponseDto(this.id, this.accountName, this.comment, this.time);
    }
}
