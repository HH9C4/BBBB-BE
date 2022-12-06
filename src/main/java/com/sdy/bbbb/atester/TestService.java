package com.sdy.bbbb.atester;

import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.dto.response.LoginResponseDto;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.Bookmark;
import com.sdy.bbbb.entity.RefreshToken;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import com.sdy.bbbb.jwt.JwtUtil;
import com.sdy.bbbb.jwt.TokenDto;
import com.sdy.bbbb.repository.AccountRepository;
import com.sdy.bbbb.repository.BookmarkRepository;
import com.sdy.bbbb.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestService {
    private final AccountRepository accountRepository;
    private final BookmarkRepository bookmarkRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    public GlobalResponseDto<LoginResponseDto> login(HttpServletResponse response) {

        TokenDto tokenDto = jwtUtil.createAllToken("tester");


        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail("tester");
        if (refreshToken.isPresent()) {
            RefreshToken refreshToken1 = refreshToken.get().updateToken(tokenDto.getRefreshToken());
            refreshTokenRepository.save(refreshToken1);
        } else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), "tester");
            refreshTokenRepository.save(newToken);
        }

        setHeader(response, tokenDto);
        Account account = accountRepository.findByEmail("tester").orElseThrow(()-> new CustomException(ErrorCode.NotFoundUser));

        List<String> bookmarkList = new ArrayList<>();
        List<Bookmark> bookmarks = bookmarkRepository.findBookmarksByAccountId(account.getId());
        for(Bookmark bookmark : bookmarks) {
            bookmarkList.add(bookmark.getGu().getGuName());
        }

        return GlobalResponseDto.ok("tester 로그인" , new LoginResponseDto(account, bookmarkList));
    }

    public GlobalResponseDto<LoginResponseDto> login2(HttpServletResponse response) {

        TokenDto tokenDto = jwtUtil.createAllToken("tester2");


        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail("tester2");
        if (refreshToken.isPresent()) {
            RefreshToken refreshToken1 = refreshToken.get().updateToken(tokenDto.getRefreshToken());
            refreshTokenRepository.save(refreshToken1);
        } else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), "tester2");
            refreshTokenRepository.save(newToken);
        }

        setHeader(response, tokenDto);
        Account account = accountRepository.findByEmail("tester2").orElseThrow(()-> new CustomException(ErrorCode.NotFoundUser));

        List<String> bookmarkList = new ArrayList<>();
        List<Bookmark> bookmarks = bookmarkRepository.findBookmarksByAccountId(account.getId());
        for(Bookmark bookmark : bookmarks) {
            bookmarkList.add(bookmark.getGu().getGuName());
        }

        return GlobalResponseDto.ok("tester2 로그인" , new LoginResponseDto(account, bookmarkList));
    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
            response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
            response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }
}
