package com.sdy.bbbb.controller;

import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.entity.Bookmark;
import com.sdy.bbbb.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/bookmarks/{gu}")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping
    public GlobalResponseDto createBookmark(@PathVariable String gu,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return bookmarkService.createBookmark(gu, userDetails.getAccount());
    }
}
