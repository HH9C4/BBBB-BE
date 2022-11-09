package com.sdy.bbbb.controller;

import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.response.BookmarkResponseDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.entity.Bookmark;
import com.sdy.bbbb.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/bookmarks/{gu}")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GlobalResponseDto<BookmarkResponseDto> createBookmark(@PathVariable String gu,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails){
        return bookmarkService.createBookmark(gu, userDetails.getAccount());
    }

    @DeleteMapping
    public GlobalResponseDto<BookmarkResponseDto> deleteBookmark(@PathVariable String gu,
                               @AuthenticationPrincipal UserDetailsImpl userDetails){
       return bookmarkService.deleteBookmark(gu, userDetails.getAccount());
    }
}
