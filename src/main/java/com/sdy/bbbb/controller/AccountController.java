package com.sdy.bbbb.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sdy.bbbb.dto.response.LoginResponseDto;
import com.sdy.bbbb.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @GetMapping("/user/kakao/callback")
    public LoginResponseDto<?> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {

        System.out.println("들어왔냥"+code);
        return accountService.kakaoLogin(code, response);
    }


}