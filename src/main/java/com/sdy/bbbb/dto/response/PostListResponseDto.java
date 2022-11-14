package com.sdy.bbbb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostListResponseDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isBookmarked;
    private List<PostResponseDto> postList;
}
