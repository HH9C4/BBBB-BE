package com.sdy.bbbb.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {


    // 데이터를 찾을 수 없습니다.
    NotFoundPost(HttpStatus.NOT_FOUND.value(), "P001", "게시글을 찾을 수 없습니다."),
    NotFoundComment(HttpStatus.NOT_FOUND.value(), "P002", "댓글을 찾을 수 없습니다."),
    NotFoundImage(HttpStatus.NOT_FOUND.value(), "P003", "삭제할 이미지를 찾을 수 없습니다."),
    NotFoundUser(HttpStatus.NOT_FOUND.value(), "A001", "계정을 찾을 수 없습니다."),
    NotFoundSort(HttpStatus.BAD_REQUEST.value(), "P004", "잘못된 요청입니다."),
    NotFoundGu(HttpStatus.NOT_FOUND.value(), "G001", "구를 찾을 수 없습니다."),

    // 일치하지 않습니다
    NotMatchAuthor(HttpStatus.BAD_REQUEST.value(), "A002", "작성자가 일치하지 않습니다."),

    // 실패 에러
    FailConvertImage(HttpStatus.BAD_REQUEST.value(), "I001", "이미지 파일 변환을 실패하였습니다."),

    // 데이터가 이미 존재합니다
    AlreadyCheckAlarm(HttpStatus.BAD_REQUEST.value(), "M001", "이미 알람을 확인했습니다."),
    AlreadyCancelLike(HttpStatus.BAD_REQUEST.value(), "L001", "이미 좋아요를 취소했습니다."),
    AlreadyExistsLike(HttpStatus.BAD_REQUEST.value(), "L002", "이미 좋아요를 눌렀습니다."),
    AlreadyCancelBookmark(HttpStatus.BAD_REQUEST.value(), "B001", "이미 북마크를 취소했습니다."),
    AlreadyExistsBookmark(HttpStatus.BAD_REQUEST.value(), "B002", "이미 북마크를 눌렀습니다."),
    // 토큰 관련
    UnAuthorized(HttpStatus.UNAUTHORIZED.value(), "A003", "다시 로그인해주세요.");




    private final int httpStatus;
    private final String errorCode;
    private final String message;
}
