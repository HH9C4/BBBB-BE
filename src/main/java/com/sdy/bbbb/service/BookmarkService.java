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

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final GuRepository guRepository;

    @Transactional
    public GlobalResponseDto<BookmarkResponseDto> createBookmark(String guName, Account account) {
        Gu gu = guRepository.findGuByGuName(guName).orElseThrow(
                () -> new CustomException(ErrorCode.NotFoundGu));
        if (bookmarkRepository.existsByGuAndAccount(gu, account)) {
            throw new CustomException(ErrorCode.AlreadyExistsBookmark);
        }
        Bookmark bookmark = new Bookmark(gu, account);
        bookmark.updateBookmarked(true);
        bookmarkRepository.save(bookmark);
        BookmarkResponseDto responseDto = new BookmarkResponseDto(bookmark);
        return GlobalResponseDto.ok("", responseDto);
    }

    @Transactional
    public GlobalResponseDto<BookmarkResponseDto> deleteBookmark(String guName, Account account) {
        Gu gu = guRepository.findGuByGuName(guName).orElseThrow(
                () -> new CustomException(ErrorCode.NotFoundGu));
        Bookmark bookmark = bookmarkRepository.findByGuAndAccount(gu, account).orElseThrow(
                () -> new CustomException(ErrorCode.AlreadyCancelBookmark));
        bookmark.updateBookmarked(false);
        bookmarkRepository.delete(bookmark);

        BookmarkResponseDto responseDto = new BookmarkResponseDto(bookmark);
        return GlobalResponseDto.ok("", responseDto);
    }
}
