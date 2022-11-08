package com.sdy.bbbb.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentRequestDto {
    @NotBlank
    private String comment;
    @NotBlank
    private String commentLevel;
}
