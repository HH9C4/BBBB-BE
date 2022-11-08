package com.sdy.bbbb.controller;


import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.response.*;
import com.sdy.bbbb.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MyPageController {

    private final MyPageService myPageService;

    // 알람 기능
    @GetMapping("/alarm")
    public GlobalResponseDto<List<AlarmResponseDto>> showAlarm(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.showAlarm(userDetails.getAccount());
    }

    // 알람 확인 기능
    @PostMapping("/alarm/{commentId}")
    public GlobalResponseDto<CommentResponseDto> checkAlarm(@PathVariable Long commentId,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return myPageService.checkAlarm(commentId, userDetails.getAccount());
    }

    // 내가 작성한 글
    @GetMapping("/myposts")
    public GlobalResponseDto<List<PostResponseDto>> getMyPosts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.getMyPosts(userDetails.getAccount());
    }


    // 내가 좋아요한 글
    @GetMapping("/mylikes")
    public GlobalResponseDto<List<LikeResponseDto>> getMyLikes(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.getMyLikes(userDetails.getAccount());
    }
}
