package com.sdy.bbbb.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.getHeaderToken(request, "Access");
        String refreshToken = jwtUtil.getHeaderToken(request, "Refresh");

        if(accessToken != null) {
            if(!jwtUtil.tokenValidation(accessToken)){
                jwtExceptionHandler(response, "Please Login", HttpStatus.BAD_REQUEST);
                return;
            }
            setAuthentication(jwtUtil.getEmailFromToken(accessToken));
        }else if(refreshToken != null) {
            if(!jwtUtil.refreshTokenValidation(refreshToken)){
                jwtExceptionHandler(response, "Please Login", HttpStatus.BAD_REQUEST);
                return;
            }
            setAuthentication(jwtUtil.getEmailFromToken(refreshToken));
        }else if(accessToken == null && refreshToken == null){
            jwtExceptionHandler(response, "Please Login", HttpStatus.BAD_REQUEST);
            return;
        }

        filterChain.doFilter(request,response);
    }

    public void setAuthentication(String email) {
        Authentication authentication = jwtUtil.createAuthentication(email);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void jwtExceptionHandler(HttpServletResponse response, String msg, HttpStatus status) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new GlobalResponseDto(HttpStatus.UNAUTHORIZED.toString(), msg,null));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }



}
