package com.sdy.bbbb.controller;

import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.request.PostRequestDto;
import com.sdy.bbbb.dto.response.*;
import com.sdy.bbbb.service.PostService;
import com.sdy.bbbb.util.request_enum.CategoryEnum;
import com.sdy.bbbb.util.request_enum.SortEnum;
import com.sdy.bbbb.util.request_enum.ValidEnum;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@RestController
@Validated
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
                                                          @RequestParam("category") @ValidEnum(enumClass = CategoryEnum.class) String category,
                                                          @RequestParam("sort") @ValidEnum(enumClass = SortEnum.class) String sort,
                                                          @PageableDefault(size = 5) Pageable pageable,
                                                          @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("= = = = 게시글 조회 = = = = ");
        return postService.getPost(gu, category, sort, pageable, userDetails.getAccount());
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
    public GlobalResponseDto<PostListResponseDto> searchPost(@RequestParam ("type") @Range(min = 0, max = 1) Integer type,
                                                               @RequestParam("searchWord") @NotBlank String searchWord,
                                                               @RequestParam("sort") @ValidEnum(enumClass = SortEnum.class) String sort,
                                                               @PageableDefault(size = 5) Pageable pageable,
                                                               @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("= = = = 게시글 검색 = = = = ");
        log.info(String.valueOf(pageable.getPageSize()));
        log.info(String.valueOf(pageable.getOffset()));
        log.info(String.valueOf(pageable.getPageNumber()));
        return postService.searchPost(type, searchWord, sort, pageable, userDetails.getAccount());
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
