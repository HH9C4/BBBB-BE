package com.sdy.bbbb.controller;

import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.request.PostRequestDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.dto.response.OnePostResponseDto;
import com.sdy.bbbb.dto.response.PostResponseDto;
import com.sdy.bbbb.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/posts")
@RestController
public class PostController {

    private final PostService postService;

    //게시글 생성
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public GlobalResponseDto<String> createPost(@RequestPart PostRequestDto postRequestDto,
                                                @RequestPart(required = false) List<MultipartFile> multipartFile,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        return postService.createPost(postRequestDto, multipartFile, userDetails.getAccount());
    }

    //게시글 조회
    @GetMapping
    public GlobalResponseDto<List<PostResponseDto>> getPost(@Param("gu") String gu,
                                                            @Param("sort") String sort,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.getPost(gu, sort, userDetails.getAccount());
    }

    //게시글 상세 조회
    @GetMapping("/{postId}")
    public GlobalResponseDto<OnePostResponseDto> getOnePost(@PathVariable Long postId,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.getOnePost(postId, userDetails.getAccount());
    }
    //게시글 수정
    @PutMapping("/{postId}")
    @ResponseStatus(HttpStatus.CREATED)
    public GlobalResponseDto<String> updatePost(@PathVariable Long postId,
                                                @RequestBody PostRequestDto postRequestDto,
                                                @RequestPart(required = false) List<MultipartFile> multipartFile,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException{
        return postService.updatePost(postId, postRequestDto, multipartFile, userDetails.getAccount());
    }

    //게시글 삭제
    @DeleteMapping("/{postId}")
    public GlobalResponseDto<String> deletePost(@PathVariable Long postId,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(postId, userDetails.getAccount());

    }
}
