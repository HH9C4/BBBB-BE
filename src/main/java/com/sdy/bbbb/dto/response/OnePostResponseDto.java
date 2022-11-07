package com.sdy.bbbb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.Comment;
import com.sdy.bbbb.entity.Post;

import java.util.List;

public class OnePostResponseDto extends PostResponseDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Comment> commentList;

    public OnePostResponseDto(Post post, Account account, List<String> imageUrl, boolean isLiked) {
        super(post, account, imageUrl, isLiked);
        this.commentList = post.getCommentList();
    }
}
