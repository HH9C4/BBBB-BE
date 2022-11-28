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
    private String profileImage;
    private String comment;
    private String createdAt;
    private Integer likeCount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean isChecked;

    private Boolean isLiked;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.accountName = comment.getAccount().getAccountName();
        this.profileImage = comment.getAccount().getProfileImage();
        this.comment = comment.isHide() ? "신고 누적으로 비공개 처리 되었습니다." : comment.getComment();
        this.createdAt = Chrono.timesAgo(comment.getCreatedAt());
        this.likeCount = comment.getLikeCount();
        this.isChecked = comment.isChecked();
    }

    public CommentResponseDto(Comment comment, boolean isLiked) {
        this.commentId = comment.getId();
        this.accountName = comment.getAccount().getAccountName();
        this.profileImage = comment.getAccount().getProfileImage();
        this.comment = comment.isHide() ? "신고 누적으로 비공개 처리 되었습니다." : comment.getComment();
        this.createdAt = Chrono.timesAgo(comment.getCreatedAt());
        this.likeCount = comment.getLikeCount();
        this.isChecked = comment.isChecked();
        this.isLiked = isLiked;
    }

}
