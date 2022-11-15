package com.sdy.bbbb.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointException implements AuthenticationEntryPoint {

    //인증 예외
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(
                new ObjectMapper().writeValueAsString(
                        new GlobalResponseDto(HttpStatus.UNAUTHORIZED.toString(), "로그인이 필요합니다",null)
                )
        );
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}