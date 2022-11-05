package com.sdy.bbbb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private String accountName;
    private String comment;
    private String createdAt;
}
