package com.sdy.bbbb.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.dto.response.LoginResponseDto;
import com.sdy.bbbb.service.AccountService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;


    //소셜 카카오 로그인
    @ApiOperation(value = "카카오 로그인", notes = "설명")
    @GetMapping("/user/kakao/callback")
    public GlobalResponseDto<?> kakaoLogin(@RequestParam String code,
                                           HttpServletResponse response) throws JsonProcessingException {

        return accountService.kakaoLogin(code, response);
    }

    //로그아웃
    @ApiOperation(value = "kakao logout api info", notes = "logout")
    @DeleteMapping("api/logout")
    public GlobalResponseDto<String> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return accountService.logout(userDetails.getAccount());
    }

}