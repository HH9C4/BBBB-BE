package com.sdy.bbbb.service;

import com.sdy.bbbb.dto.response.BookmarkResponseDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.Bookmark;
import com.sdy.bbbb.entity.Gu;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import com.sdy.bbbb.repository.BookmarkRepository;
import com.sdy.bbbb.repository.GuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final GuRepository guRepository;

    @Transactional
    public GlobalResponseDto<BookmarkResponseDto> createBookmark(String guName, Account account) {
        Gu gu = guRepository.findGuByGuName(guName).orElseThrow(
                () -> new CustomException(ErrorCode.NotFoundGu));
        // 구 없으면 에러처리
        if (bookmarkRepository.existsByGuAndAccount(gu, account)) {
            throw new CustomException(ErrorCode.AlreadyExistsBookmark);
            // 북마크 정보 이미 있는 상태 -> 예러 처리
        }
        Bookmark bookmark = new Bookmark(gu, account);
        // 북마크 생성
        bookmark.updateBookmarked(true);
        // true 변환
        bookmarkRepository.save(bookmark);
        // 북마크 저장
        BookmarkResponseDto responseDto = new BookmarkResponseDto(bookmark);
        return GlobalResponseDto.ok("", responseDto);
    }

    @Transactional
    public GlobalResponseDto<BookmarkResponseDto> deleteBookmark(String guName, Account account) {
        Gu gu = guRepository.findGuByGuName(guName).orElseThrow(
                () -> new CustomException(ErrorCode.NotFoundGu));
        // 구 이름으로 찾을 수 있나 모르겠음, 없으면 에러처리
        Optional<Bookmark> foundBookmark = bookmarkRepository.findByGuAndAccount(gu, account);
        // 삭제 할 북마크 찾아오기
        if (foundBookmark.isPresent()){
            foundBookmark.get().updateBookmarked(false);
            // false 변환
            bookmarkRepository.delete(foundBookmark.get());
            // 북마크 삭제
        }else {
            throw new CustomException(ErrorCode.AlreadyCancelBookmark);
            // 북마크 정보가 없는 상태 예외처리
        }
        BookmarkResponseDto responseDto = new BookmarkResponseDto(foundBookmark.get());
        return GlobalResponseDto.ok("", responseDto);
    }
}
