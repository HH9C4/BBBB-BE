package com.sdy.bbbb.controller;

import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.response.BookmarkResponseDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.entity.Bookmark;
import com.sdy.bbbb.service.BookmarkService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/bookmarks/{gu}")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @ApiOperation(value = "북마크 생성", notes = "설명")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GlobalResponseDto<BookmarkResponseDto> createBookmark(@PathVariable String gu,
                                                                 @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails){
        return bookmarkService.createBookmark(gu, userDetails.getAccount());
    }
    @ApiOperation(value = "북마크 삭제", notes = "설명")
    @DeleteMapping
    public GlobalResponseDto<BookmarkResponseDto> deleteBookmark(@PathVariable String gu,
                                                                 @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails){
       return bookmarkService.deleteBookmark(gu, userDetails.getAccount());
    }
}
