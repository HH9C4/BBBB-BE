package com.sdy.bbbb.controller;

import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.request.PostRequestDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.dto.response.OnePostResponseDto;
import com.sdy.bbbb.dto.response.PostListResponseDto;
import com.sdy.bbbb.dto.response.PostResponseDto;
import com.sdy.bbbb.service.PostService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/posts")
@RestController
public class PostController {

    private final PostService postService;

    //게시글 생성
    @ApiOperation(value = "게시글 생성 create new post", notes = "create new post with PostRequestDto, Images")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public GlobalResponseDto<PostResponseDto> createPost(@RequestPart(name = "contents") @Valid PostRequestDto postRequestDto,
                                                         @RequestPart(name = "imageList", required = false) List<MultipartFile> multipartFile,
                                                         @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return postService.createPost(postRequestDto, multipartFile, userDetails.getAccount());
    }

    //게시글 조회
    @ApiOperation(value = "게시글 조회 ", notes = "설명")
    @GetMapping
    public GlobalResponseDto<PostListResponseDto> getPost(@RequestParam("gu") String gu,
                                                          @RequestParam("sort") String sort,
                                                          @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return postService.getPost(gu, sort, userDetails.getAccount());
    }

    //게시글 상세 조회
    @ApiOperation(value = "게시글 상세 조회", notes = "설명")
    @GetMapping("/{postId}")
    public GlobalResponseDto<OnePostResponseDto> getOnePost(@PathVariable Long postId,
                                                            @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return postService.getOnePost(postId, userDetails.getAccount());
    }

    //게시글 검색
    @ApiOperation(value = "게시글 검색", notes = "설명")
    @GetMapping("/search")
    public GlobalResponseDto<List<PostResponseDto>> searchPost(@RequestParam("searchWord") String searchWord,
                                                               @RequestParam("sort") String sort,
                                                               @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return postService.searchPost(searchWord, sort, userDetails.getAccount());
    }

    //게시글 수정
    @ApiOperation(value = "게시글 수정", notes = "설명")
    @PutMapping(value = "/{postId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public GlobalResponseDto<PostResponseDto> updatePost(@PathVariable Long postId,
                                                @RequestPart(name = "contents") @Valid PostRequestDto postRequestDto,
                                                @RequestPart(name = "imageList", required = false) List<MultipartFile> multipartFile,
                                                @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return postService.updatePost(postId, postRequestDto, multipartFile, userDetails.getAccount());
    }

    //게시글 삭제
    @ApiOperation(value = "게시글 삭제", notes = "설명")
    @DeleteMapping("/{postId}")
    public GlobalResponseDto<String> deletePost(@PathVariable Long postId,
                                                @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return postService.deletePost(postId, userDetails.getAccount());
    }


}
