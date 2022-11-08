package com.sdy.bbbb.controller;

import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.request.LikeRequestDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes/{postId}")
public class LikeController {
    private final LikeService likeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GlobalResponseDto createLike(@PathVariable Long postId,
                              @ModelAttribute LikeRequestDto requestDto,
                              @AuthenticationPrincipal UserDetailsImpl userDetails){
        return likeService.createLike(postId, requestDto.getLiketLevel(),  userDetails.getAccount());
    }
    @DeleteMapping
    public void deleteLike(@PathVariable Long postId,
                              @AuthenticationPrincipal UserDetailsImpl userDetails){
        likeService.deleteLike(postId, userDetails.getAccount());
    }
}
