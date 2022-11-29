package com.sdy.bbbb.controller;


import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.request.UpdateRequestDto;
import com.sdy.bbbb.dto.response.*;
import com.sdy.bbbb.service.MyPageService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MyPageController {

    private final MyPageService myPageService;

    // 알람 기능
    @ApiOperation(value = "alarm function", notes = "when users get new comments on thier own posts, they will get alarms")
    @GetMapping("/alarm")
    public GlobalResponseDto<List<AlarmResponseDto>> showAlarm(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.showAlarm(userDetails.getAccount());
    }

    // 알람 확인 기능
    @ApiOperation(value = "alarm check function", notes = "when users check their alarm, alarm state is going to be changed")
    @PostMapping("/alarm/{commentId}")
    public GlobalResponseDto<CommentResponseDto> checkAlarm(@PathVariable Long commentId,
                                                            @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.checkAlarm(commentId, userDetails.getAccount());
    }

    // 내가 작성한 글
    @ApiOperation(value = "list of users own posts", notes = "users can see their own posts")
    @GetMapping("/myposts")
    public GlobalResponseDto<List<PostResponseDto>> getMyPosts(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.getMyPosts(userDetails.getAccount());
    }


    // 내가 좋아요한 글
    @ApiOperation(value = "list of users own liked posts", notes = "users can see thier own liked posts")
    @GetMapping("/mylikes")
    public GlobalResponseDto<List<PostResponseDto>> getMyLikes(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.getMyLikes(userDetails.getAccount());
    }

    // 내가 누른 북마크
    @ApiOperation(value = "내가 누른 북마크", notes = "설명")
    @GetMapping("/mybookmarks")
    public GlobalResponseDto<List<BookmarkResponseDto>> getMyBookmarks(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.getMyBookmarks(userDetails.getAccount());
    }

    // 닉네임, 프로필 사진 수정
    @ApiOperation(value = "닉네임, 프로필 사진 수정", notes = "유저 정보 중 닉네임과 프로필 사진만 수정이 가능합니다.")
    @PutMapping(value = "/myinfo", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public GlobalResponseDto<LoginResponseDto> updateMyInfo(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @RequestPart(name = "nickname", required = false) UpdateRequestDto updateRequestDto,
                                                            @RequestPart(name = "image", required = false) MultipartFile multipartFile) {
        return myPageService.updateMyInfo(userDetails.getAccount(), updateRequestDto, multipartFile);
    }

    // 닉네임 중복확인 기능
    @ApiOperation(value = "닉네임 중복확인 기능", notes = "닉네임 중복확인 기능입니다.")
    @GetMapping(value = "/namecheck")
    public GlobalResponseDto<UpdateRequestDto> checkNickname(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                             @RequestBody UpdateRequestDto updateRequestDto) {
        return myPageService.checkNickname(userDetails.getAccount(), updateRequestDto);
    }
}
