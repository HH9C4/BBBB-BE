package com.sdy.bbbb.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customExceptionHandle(CustomException e) {

        return ErrorResponse.toResponseEntity(e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationExceptions(MethodArgumentNotValidException e) {
        List<ErrorResponse> errors = new ArrayList<>();

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()){
            errors.add(ErrorResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .errorCode(fieldError.getCode())
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build()
            );
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    // test
    @RequiredArgsConstructor
    @Getter
    @Builder
    private static class ErrorResponse {
        private final int httpStatus;
        private final String errorCode;
        private final String field;
        private final String message;

        public static ResponseEntity toResponseEntity(CustomException e) {
            return ResponseEntity
                    .status(e.getErrorCode().getHttpStatus())
                    .body(ErrorResponse.builder()
                            .httpStatus(e.getErrorCode().getHttpStatus())
                            .errorCode(e.getErrorCode().getErrorCode())
                            .message(e.getErrorCode().getMessage())
                            .build()
                    );
        }
    }
}
