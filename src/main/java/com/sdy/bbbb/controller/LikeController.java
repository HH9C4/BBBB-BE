package com.sdy.bbbb.controller;

import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.request.LikeRequestDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.service.LikeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {
    private final LikeService likeService;

    @ApiOperation(value = "게시글 좋아요 생성", notes = "설명")
    @PostMapping("/{postId}")
    @ResponseStatus(HttpStatus.CREATED)
    public GlobalResponseDto createPostLike(@PathVariable Long postId,
                                        @RequestBody LikeRequestDto requestDto,
                                        @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails){
        return likeService.createPostLike(postId, requestDto.getLiketLevel(), userDetails.getAccount());
    }

    @ApiOperation(value = "게시글 좋아요 삭제", notes = "설명")
    @DeleteMapping("/{postId}")
    public GlobalResponseDto deletePostLike(@PathVariable Long postId,
                                        @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails){
        return likeService.deletePostLike(postId, userDetails.getAccount());
    }
    @ApiOperation(value = "댓글 좋아요 생성", notes = "설명")
    @PostMapping("/{commentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public GlobalResponseDto createCommentLike(@PathVariable Long commentId,
                                        @RequestBody LikeRequestDto requestDto,
                                        @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails){
        return likeService.createCommentLike(commentId, requestDto.getLiketLevel(), userDetails.getAccount());
    }

    @ApiOperation(value = "댓글 좋아요 삭제", notes = "설명")
    @DeleteMapping("/{commentId}")
    public GlobalResponseDto deleteCommentLike(@PathVariable Long commentId,
                                        @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails){
        return likeService.deleteCommentLike(commentId, userDetails.getAccount());
    }

}
