package com.sdy.bbbb.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeResponseDto {

    private Boolean isLiked;

    private Integer likeCount;

    public LikeResponseDto(Boolean isLiked, Integer likeCount) {
        this.isLiked = isLiked;
        this.likeCount = likeCount;
    }

}
