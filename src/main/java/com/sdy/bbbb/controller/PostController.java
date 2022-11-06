package com.sdy.bbbb.controller;

import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.request.PostRequestDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.dto.response.PostResponseDto;
import com.sdy.bbbb.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/posts")
@RestController
public class PostController {

    private final PostService postService;

    //게시글 작성
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GlobalResponseDto<String> createPost(@RequestBody PostRequestDto postRequestDto,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(postRequestDto, userDetails.getAccount());
    }

    //게시글 조회
    @GetMapping("/{gu}/{sort}")
    public GlobalResponseDto<List<PostResponseDto>> getPost(@PathVariable String gu,
                                                            @PathVariable String sort,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.getPost(gu, sort, userDetails.getAccount());
    }

    //게시글 수정
    @PutMapping("/{postId}")
    public GlobalResponseDto<String> updatePost(@PathVariable Long postId,
                                                @RequestBody PostRequestDto postRequestDto,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(postId, postRequestDto, userDetails.getAccount());
    }

    //게시글 삭제
    @DeleteMapping("/{postId}")
    public GlobalResponseDto<String> deletePost(@PathVariable Long postId,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(postId, userDetails.getAccount());

    }
}
