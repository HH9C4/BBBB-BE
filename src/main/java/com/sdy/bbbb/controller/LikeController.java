package com.sdy.bbbb.controller;

import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.dto.response.LikeResponseDto;
import com.sdy.bbbb.service.LikeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {
    private final LikeService likeService;

    @ApiOperation(value = "", notes = "")
    @PutMapping
    public  GlobalResponseDto<LikeResponseDto> like(@RequestParam Long id,
                                                    @RequestParam @Range(min = 1, max = 2)Integer level,
                                                    @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.like(id, level, userDetails.getAccount());
    }

    @ApiOperation(value = "좋아요 생성 create like", notes = "create like on the post")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GlobalResponseDto<LikeResponseDto> createPostLike(@RequestParam Long id,
                                                             @RequestParam Integer level,
                                                             @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.createPostLike(id, level, userDetails.getAccount());
    }


    @ApiOperation(value = "좋아요 삭제 cancle like", notes = "cancle like on the post")
    @DeleteMapping
    public GlobalResponseDto<LikeResponseDto> deletePostLike(@RequestParam Long id,
                                            @RequestParam Integer level,
                                            @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.deletePostLike(id, level, userDetails.getAccount());
    }
}
