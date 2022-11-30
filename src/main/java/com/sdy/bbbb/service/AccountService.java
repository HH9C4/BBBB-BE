package com.sdy.bbbb.service;

import com.sdy.bbbb.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final JwtUtil jwtUtil;

    public void asd(){
//        jwtUtil.refreshTokenValidation();
    }
}
