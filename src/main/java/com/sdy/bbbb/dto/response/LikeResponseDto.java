package com.sdy.bbbb.dto.response;

import com.sdy.bbbb.entity.Like;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeResponseDto {

    private Long postId;

    public LikeResponseDto(Like like){

        this.postId = like.getPost().getId();

    }
}
