package com.sdy.bbbb.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeResponseDto {

    private Boolean isLiked;

    public LikeResponseDto(Boolean isLiked) {
        this.isLiked = isLiked;
    }

}
