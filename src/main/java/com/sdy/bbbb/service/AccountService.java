package com.sdy.bbbb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.request.KakaoUserInfoDto;
import com.sdy.bbbb.dto.response.LoginResponseDto;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.RefreshToken;
import com.sdy.bbbb.jwt.JwtUtil;
import com.sdy.bbbb.jwt.TokenDto;
import com.sdy.bbbb.repository.AccountRepository;
import com.sdy.bbbb.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;


    @Autowired
    public AccountService(RefreshTokenRepository refreshTokenRepository, JwtUtil jwtUtil, AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDto<?> kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

        // 2. "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        // 3. "카카오 사용자 정보"로 필요시 회원가입
        Account kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

        // 4. 강제 로그인 처리
        forceLogin(kakaoUser);

        //토큰 발급
        TokenDto tokenDto = jwtUtil.createAllToken(kakaoUserInfo.getEmail());

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail(kakaoUserInfo.getEmail());

        // 로그아웃한 후 로그인을 다시 하는가?
        if(refreshToken.isPresent()) {
            RefreshToken refreshToken1 = refreshToken.get().updateToken(tokenDto.getRefreshToken());
            refreshTokenRepository.save(refreshToken1);
        } else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), kakaoUserInfo.getEmail());
            refreshTokenRepository.save(newToken);
        }

        //토큰을 header에 넣어서 클라이언트에게 전달하기
        setHeader(response, tokenDto);

        return new LoginResponseDto<>("200", kakaoUserInfo.getNickname()+"님 환영합니다.","닉네임" + kakaoUserInfo.getNickname());
    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }

    private String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "a8c29f43cc985001f5fcd08bcbd9bbac");
        body.add("redirect_uri", "http://localhost:8080/user/kakao/callback");
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();
        String profileImage = jsonNode.get("profile_image")
                .get("profileImage").asText();
        return new KakaoUserInfoDto(id, nickname, email, profileImage);
    }

    private Account registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getId();
        Account kakaoUser = accountRepository.findByKakaoId(kakaoId)
                .orElse(null);
        if (kakaoUser == null) {
            // 카카오 사용자 이메일과 동일한 이메일을 가진 회원이 있는지 확인
            String kakaoEmail = kakaoUserInfo.getEmail();
            Account sameEmailUser = accountRepository.findByEmail(kakaoEmail).orElse(null);
            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser;
                // 기존 회원정보에 카카오 Id 추가
                kakaoUser.setKakaoId(kakaoId);
            } else {
                // 신규 회원가입
                // username: kakao nickname
                String nickname = kakaoUserInfo.getNickname();

                // password: random UUID
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                // email: kakao email
                String email = kakaoUserInfo.getEmail();
                // role: 일반 사용자
//                UserRoleEnum role = UserRoleEnum.USER;

                // 프로필 사진 가져오기
                String profileImage = kakaoUserInfo.getProfileImage();

                kakaoUser = new Account(nickname, encodedPassword, email, profileImage, kakaoId);
            }

            accountRepository.save(kakaoUser);
        }
        return kakaoUser;
    }

    private void forceLogin(Account kakaoUser) {
        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}