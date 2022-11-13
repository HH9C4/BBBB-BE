package com.sdy.bbbb.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;


@Getter
public class CommentRequestDto {

    @NotBlank(message = "comment는 공백이거나 null일 수 없습니다.")
    private String comment;

    @NotBlank(message = "commentLevel은 공백이거나 null일 수 없습니다.")
    private Integer commentLevel;

}
