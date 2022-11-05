package com.sdy.bbbb.controller;

import com.sdy.bbbb.dto.request.CommentRequestDto;
import com.sdy.bbbb.dto.response.CommentResponseDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comments/{postId}")
    public GlobalResponseDto createComment(@RequestBody CommentRequestDto requestDto,@PathVariable Long postId){
        // 유저 아이디도 받아가야함
        return commentService.createComment(requestDto, postId);
    }
}
