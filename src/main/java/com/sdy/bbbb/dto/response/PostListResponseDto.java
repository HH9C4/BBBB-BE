package com.sdy.bbbb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostListResponseDto {
    private Boolean isBookmarked;
    private List<PostResponseDto> postList;
}
