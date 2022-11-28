package com.sdy.bbbb.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

@Getter
public class ReportRequestDto {

    @Range(min = 1, max = 3)
    private Long level;
    @NotBlank()
    private Long reporterId;
    @NotBlank()
    private Long reportedId;
    @NotBlank(message = "내용은 공백일 수 없습니다.")
    private String content;

    public ReportRequestDto(Long level, Long reporterId, Long reportedId, String content) {
        this.level = level;
        this.reporterId = reporterId;
        this.reportedId = reportedId;
        this.content = content;
    }
}
