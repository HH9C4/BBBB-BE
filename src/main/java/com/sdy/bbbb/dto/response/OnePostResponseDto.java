package com.sdy.bbbb.dto.response;

import com.sdy.bbbb.entity.Post;
import lombok.Getter;

import java.util.List;

@Getter
public class OnePostResponseDto extends PostResponseDto {

    private List<CommentResponseDto> commentList;

    public OnePostResponseDto(Post post, List<String> imageUrl, boolean isLiked, List<CommentResponseDto> commentList) {
        super(post, imageUrl, isLiked);
        this.commentList = commentList;
    }

}
