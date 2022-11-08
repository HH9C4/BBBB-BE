package com.sdy.bbbb.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class CommentRequestDto {
    @NotBlank
    private String comment;
    @NotBlank
    private String commentLevel;
}
