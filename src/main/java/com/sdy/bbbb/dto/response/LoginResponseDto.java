package com.sdy.bbbb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@Builder
public class LoginResponseDto<T> {
    private String status;
    private String msg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;


    public static <T> LoginResponseDto<T> ok(String msg, T data) {
        return new LoginResponseDto<>(HttpStatus.OK.toString(), msg, data);
    }

}
