package com.sdy.bbbb.dto.response;

import com.sdy.bbbb.util.Chrono;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private String accountName;
    private String comment;
    private String createdAt;

    public CommentResponseDto(Long id, String accountName, String comment, LocalDateTime time){
        this.commentId = id;
        this.accountName = accountName;
        this.comment = comment;
        this.createdAt = Chrono.timesAgo(time);
    }
}
