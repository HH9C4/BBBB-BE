package com.sdy.bbbb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sdy.bbbb.entity.Comment;
import com.sdy.bbbb.util.Chrono;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private String accountName;
    private String comment;
    private String createdAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean isChecked;

    public CommentResponseDto(Long id, String accountName, String comment, LocalDateTime time){
        this.commentId = id;
        this.accountName = accountName;
        this.comment = comment;
        this.createdAt = Chrono.timesAgo(time);
    }

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.accountName = comment.getAccount().getUsername();
        this.comment = comment.getComment();
        this.createdAt = Chrono.timesAgo(comment.getCreatedAt());
        this.isChecked = comment.isChecked();
    }
}
