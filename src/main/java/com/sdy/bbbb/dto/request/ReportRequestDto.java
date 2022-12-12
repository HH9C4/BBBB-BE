package com.sdy.bbbb.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class ReportRequestDto {

    @Range(min = 1, max = 3)
    private Long level;

    @NotNull(message = "신고할 id는 공백일 수 없습니다.")
    private String reportedId;

    @NotBlank(message = "내용은 공백일 수 없습니다.")
    private String content;

}
