package com.sdy.bbbb.controller;

import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.request.CommentRequestDto;
import com.sdy.bbbb.dto.response.CommentResponseDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.service.CommentService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    @ApiOperation(value = "댓글 생성 create comment", notes = "create new comment with postId, CommentRequestDto(comment), Account")
    @PostMapping("/{postId}")
    @ResponseStatus(HttpStatus.CREATED)
    public GlobalResponseDto<CommentResponseDto> createComment(@PathVariable Long postId,
                                                               @RequestBody CommentRequestDto requestDto,
                                                               @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(postId, requestDto, userDetails.getAccount());
    }

    @ApiOperation(value = "댓글 삭제 delete comment", notes = "delete comment, users can only delete their own comments")
    @DeleteMapping("/{commentId}")
    public GlobalResponseDto deleteComment(@PathVariable Long commentId,
                                           @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(commentId, userDetails.getAccount());
    }
}
