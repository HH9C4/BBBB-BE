package com.sdy.bbbb.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UpdateRequestDto {

    @NotBlank(message = "닉네임은 공백일 수 없습니다.")
    private String nickname;

}
