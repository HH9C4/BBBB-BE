package com.sdy.bbbb.controller;

import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.request.CommentRequestDto;
import com.sdy.bbbb.dto.response.CommentResponseDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.service.CommentService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    @ApiOperation(value = "댓글 생성", notes = "api 설명")
    @PostMapping("/{postId}")
    public GlobalResponseDto createComment(@PathVariable Long postId, @RequestBody CommentRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.createComment(postId, requestDto, userDetails.getAccount());
    }
    @ApiOperation(value = "댓글 삭제", notes = "api 설명")
    @DeleteMapping("/{commentId}")
    public GlobalResponseDto deleteComment(@PathVariable Long commentId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(commentId, userDetails.getAccount());
    }
}
