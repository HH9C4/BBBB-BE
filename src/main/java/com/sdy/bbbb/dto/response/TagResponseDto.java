package com.sdy.bbbb.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class TagResponseDto {

    private List<String> tagList;

    public TagResponseDto(List<String> tagList) {
        this.tagList = tagList;
    }
}
