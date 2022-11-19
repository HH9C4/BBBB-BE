package com.sdy.bbbb.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.UnexpectedTypeException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {
    @Value("${spring.servlet.multipart.max-request-size}")
    String maxSize;

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customExceptionHandle(CustomException e) {

        return ErrorResponse.toResponseEntity(e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationExceptions(MethodArgumentNotValidException e) {
        List<ErrorResponse> errors = new ArrayList<>();

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
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

    @ExceptionHandler(SizeLimitExceededException.class)
    public ResponseEntity<String> handleSizeLimitException(SizeLimitExceededException e) {


        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("이미지 용량은 " + maxSize + " 를 초과할 수 없습니다.");
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<ErrorResponse> handleUnExpectedTypeException(UnexpectedTypeException e){

        return ErrorResponse.toResponseEntity(HttpStatus.BAD_REQUEST, "C001", "요청 인자가 올바르지 않습니다.");
    }


    @RequiredArgsConstructor
    @Getter
    @Builder
    private static class ErrorResponse {
        private final int httpStatus;
        private final String errorCode;
        @JsonInclude(JsonInclude.Include.NON_NULL)
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

        public static ResponseEntity toResponseEntity(HttpStatus status, String errorCode, String message) {
            return ResponseEntity
                    .status(status)
                    .body(ErrorResponse.builder()
                            .httpStatus(status.value())
                            .errorCode(errorCode)
                            .message(message)
                            .build()
                    );
        }
    }
}
