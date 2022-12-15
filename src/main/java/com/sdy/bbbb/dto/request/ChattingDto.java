package com.sdy.bbbb.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter @Setter
@NoArgsConstructor
public class ChattingDto {

    @NotNull(message = "보내는 사람이 없습니다.")
    private String sender;

    @NotNull(message = "보내는 메세지가 없습니다.")
    private String message;

    private String sendTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

}
