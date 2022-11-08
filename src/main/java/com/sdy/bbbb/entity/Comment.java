package com.sdy.bbbb.entity;

import com.sdy.bbbb.dto.request.CommentRequestDto;
import com.sdy.bbbb.dto.response.CommentResponseDto;
import io.swagger.annotations.ApiModelProperty;
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
    private String accountName;
    private LocalDateTime time = LocalDateTime.now();
    private boolean isChecked;
    @ManyToOne
    @JoinColumn
    @ApiModelProperty(hidden = true)
    private Account account;
    @ManyToOne
    @JoinColumn
    private Post post;


    public Comment(CommentRequestDto requestDto, Post post, Account account) {
        this.account = account;
        this.post = post;
        this.accountName=account.getAccountName();
        this.comment = requestDto.getComment();
    }
    public CommentResponseDto responseDto(){
        return new CommentResponseDto(this.id, this.accountName, this.comment, this.time);
    }
}
