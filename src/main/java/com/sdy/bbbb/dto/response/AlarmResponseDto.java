package com.sdy.bbbb.dto.response;


import com.sdy.bbbb.entity.Comment;
import com.sdy.bbbb.util.Chrono;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlarmResponseDto {

    private Long postId;

    private String content;

    private Long commentId;

    private String accountName;

    private String comment;

    private String createdAt;

    private boolean isChecked;

    public AlarmResponseDto(Comment comment) {
        this.postId = comment.getPost().getId();
        this.content = comment.getPost().getContent();
        this.commentId = comment.getId();
        this.accountName = comment.getAccount().getAccountName();
        this.comment = comment.getComment();
        this.createdAt = Chrono.timesAgo(comment.getCreatedAt());
        this.isChecked = comment.isChecked();
    }

}
