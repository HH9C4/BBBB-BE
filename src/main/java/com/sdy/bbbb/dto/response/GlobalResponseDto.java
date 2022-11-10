package com.sdy.bbbb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@Builder
public class GlobalResponseDto<T> {
    private String status;
    private String msg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;


    public static <T> GlobalResponseDto <T> ok(String msg, T data){
        return new GlobalResponseDto<>(HttpStatus.OK.toString(), msg, data);
    }
    public static <T> GlobalResponseDto <T> created(String msg, T data){
        return new GlobalResponseDto<>(HttpStatus.CREATED.toString(), msg, data);
    }
    public static <T> GlobalResponseDto <T> fail(String msg){
        return new GlobalResponseDto<>(HttpStatus.BAD_REQUEST.toString(),msg, null);
    }
}
