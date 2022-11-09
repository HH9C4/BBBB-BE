package com.sdy.bbbb.service;

import com.sdy.bbbb.config.UserDetailsImpl;
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
    public GlobalResponseDto createBookmark(String guName, Account account) {
        Gu gu = guRepository.findByGuName(guName).orElseThrow(
                () -> new CustomException(ErrorCode.NotFound));
        // 구 이름으로 찾을 수 있는지 테스트, 없으면 에러처리
        Bookmark bookmark = new Bookmark(gu, account);
        bookmarkRepository.save(bookmark);
        // 북마크 저장
        if (bookmarkRepository.existsByGuAndAccount(gu,account)) {
            bookmark.updateBookmarked(true);
            // 로그인 유저 정보 있으면 북마크 true (여기서 하면 장단점 확인)
        } return GlobalResponseDto.created("");
    }
}
