package com.sdy.bbbb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sdy.bbbb.entity.Comment;
import com.sdy.bbbb.util.Chrono;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private String accountName;
    private String comment;
    private String createdAt;
    private Integer likeCount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean isChecked;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.accountName = comment.getAccount().getAccountName();
        this.comment = comment.getComment();
        this.createdAt = Chrono.timesAgo(comment.getCreatedAt());
        this.likeCount = comment.getLikeCount();
        this.isChecked = comment.isChecked();
    }
}
