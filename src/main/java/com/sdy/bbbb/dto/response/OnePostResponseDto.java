package com.sdy.bbbb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.Comment;
import com.sdy.bbbb.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
public class OnePostResponseDto extends PostResponseDto {
    private List<CommentResponseDto> commentList;

    public OnePostResponseDto(Post post, Account account, List<String> imageUrl, boolean isLiked, List<CommentResponseDto> commentList) {
        super(post, account, imageUrl, isLiked);
        this.commentList = commentList;
    }
}
