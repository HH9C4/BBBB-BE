package com.sdy.bbbb.controller;

import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.service.SSE.SseService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SseController {

    private final SseService sseService;

    //SSE 구독 요청
    @ApiOperation(value = "SSE 구독 요청", notes = "실시간 알림을 받기 위한 구독 신청")
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return sseService.subscribe(userDetails.getAccount().getId());
    }

}
