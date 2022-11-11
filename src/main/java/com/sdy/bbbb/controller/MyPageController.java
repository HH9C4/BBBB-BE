package com.sdy.bbbb.controller;


import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.response.*;
import com.sdy.bbbb.service.MyPageService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MyPageController {

    private final MyPageService myPageService;

    // 알람 기능
    @ApiOperation(value = "알림 기능", notes = "설명")
    @GetMapping("/alarm")
    public GlobalResponseDto<List<AlarmResponseDto>> showAlarm(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.showAlarm(userDetails.getAccount());
    }

    // 알람 확인 기능
    @ApiOperation(value = "알림 확인 기능", notes = "설명")
    @PostMapping("/alarm/{commentId}")
    public GlobalResponseDto<CommentResponseDto> checkAlarm(@PathVariable Long commentId,
                                                            @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.checkAlarm(commentId, userDetails.getAccount());
    }

    // 내가 작성한 글
    @ApiOperation(value = "내가 작성한 글", notes = "설명")
    @GetMapping("/myposts")
    public GlobalResponseDto<List<PostResponseDto>> getMyPosts(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.getMyPosts(userDetails.getAccount());
    }


    // 내가 좋아요한 글
    @ApiOperation(value = "내가 좋아요한 글", notes = "설명")
    @GetMapping("/mylikes")
    public GlobalResponseDto<List<PostResponseDto>> getMyLikes(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.getMyLikes(userDetails.getAccount());
    }

    // 내가 누른 북마크
    @ApiOperation(value = "내가 누른 북마크", notes = "설명")
    @GetMapping("mybookmarks")
    public GlobalResponseDto<List<BookmarkResponseDto>> getMyBookmarks(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.getMyBookmarks(userDetails.getAccount());
    }
}
