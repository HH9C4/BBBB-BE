package com.sdy.bbbb.controller;

import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.request.PostRequestDto;
import com.sdy.bbbb.dto.response.*;
import com.sdy.bbbb.service.PostService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@Slf4j
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
        log.info("= = = = 게시글 생성 = = = = ");
        return postService.createPost(postRequestDto, multipartFile, userDetails.getAccount());
    }

    //게시글 조회
    @ApiOperation(value = "게시글 조회 ", notes = "get post with \"gu\" ,\"sort\"")
    @GetMapping
    public GlobalResponseDto<PostListResponseDto> getPost(@RequestParam("gu") String gu,
                                                          @RequestParam("category") String category,
                                                          @RequestParam("sort") String sort,
                                                          @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("= = = = 게시글 조회 = = = = ");
        return postService.getPost(gu, category, sort, userDetails.getAccount());
    }

    //게시글 상세 조회
    @ApiOperation(value = "게시글 상세 조회", notes = "설명")
    @GetMapping("/{postId}")
    public GlobalResponseDto<OnePostResponseDto> getOnePost(@PathVariable Long postId,
                                                            @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("= = = = 게시글 상세 조회 = = = = ");
        return postService.getOnePost(postId, userDetails.getAccount());
    }

    //게시글 검색
    @ApiOperation(value = "게시글 검색", notes = "설명")
    @GetMapping("/search")
    public GlobalResponseDto<List<PostResponseDto>> searchPost(@RequestParam ("type") Integer type,
                                                               @RequestParam("searchWord") String searchWord,
                                                               @RequestParam("sort") String sort,
                                                               @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("= = = = 게시글 검색 = = = = ");
        return postService.searchPost(type, searchWord, sort, userDetails.getAccount());
    }

    //핫태그 검색
    @ApiOperation(value = "핫 태그 조회", notes = "설명")
    @GetMapping("/hottag")
    public GlobalResponseDto<TagResponseDto> hotTag(@RequestParam("gu") String guName){
        log.info("= = = = 핫태그 검색 = = = = ");
        return postService.hotTag20(guName);
    }

    //게시글 수정
    @ApiOperation(value = "게시글 수정", notes = "설명")
    @PutMapping(value = "/{postId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public GlobalResponseDto<PostResponseDto> updatePost(@PathVariable Long postId,
                                                @RequestPart(name = "contents") @Valid PostRequestDto postRequestDto,
                                                @RequestPart(name = "imageList", required = false) List<MultipartFile> multipartFile,
                                                @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("= = = = 게시글 수정 = = = = ");
        return postService.updatePost(postId, postRequestDto, multipartFile, userDetails.getAccount());
    }

    //게시글 삭제
    @ApiOperation(value = "게시글 삭제", notes = "설명")
    @DeleteMapping("/{postId}")
    public GlobalResponseDto<String> deletePost(@PathVariable Long postId,
                                                @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("= = = = 게시글 삭제 = = = = ");
        return postService.deletePost(postId, userDetails.getAccount());
    }


}
