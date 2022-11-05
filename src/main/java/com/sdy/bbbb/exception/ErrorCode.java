package com.sdy.bbbb.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    NotFound(HttpStatus.NOT_FOUND.value(), "P001", "데이터를 찾을 수 없습니다."),
    NotMatch(HttpStatus.BAD_REQUEST.value(), "P002", "일치하지 않습니다."),
    AlreadyExists(HttpStatus.BAD_REQUEST.value(), "P003", "데이터가 이미 존재합니다.");

    private final int httpStatus;
    private final String errorCode;
    private final String message;
}
