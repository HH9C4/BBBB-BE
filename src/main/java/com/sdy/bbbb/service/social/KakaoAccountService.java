package com.sdy.bbbb.service.social;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.request.loginRequestDto.KakaoUserInfoDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.dto.response.LoginResponseDto;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.Bookmark;
import com.sdy.bbbb.entity.MyPage;
import com.sdy.bbbb.exception.CustomException;
import com.sdy.bbbb.exception.ErrorCode;
import com.sdy.bbbb.jwt.JwtUtil;
import com.sdy.bbbb.jwt.TokenDto;
import com.sdy.bbbb.entity.RedisRefreshToken;
import com.sdy.bbbb.redis.RedisRepository;
import com.sdy.bbbb.repository.AccountRepository;
import com.sdy.bbbb.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class KakaoAccountService {

    @Value("${kakao.rest.api.key}")
    private String kakaoApiKey;

    @Value("${kakao.admin.key}")
    private String kakaoAdminKey;

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final RedisRepository redisRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public GlobalResponseDto<LoginResponseDto> kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        String message = "??? ???????????????.";

        // 1. "?????? ??????"??? "????????? ??????" ??????
        String accessToken = getAccessToken(code);

        // 2. "????????? ??????"?????? "????????? ????????? ??????" ????????????
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        // 3. "????????? ????????? ??????"??? ????????? ????????????
        Account kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

        if (kakaoUser.getNaverId() != null) {
            message += " ????????? ???????????? ????????? ????????? ?????????????????????!";
        }

        // 4. ?????? ????????? ??????
        forceLogin(kakaoUser);


        //?????? ??????
        TokenDto tokenDto = jwtUtil.createAllToken(kakaoUserInfo.getEmail());


        //??????????????? ???????????? ????????????
        Optional<RedisRefreshToken> refreshToken3 = redisRepository.findById(kakaoUser.getEmail());

        long expiration = JwtUtil.REFRESH_TIME / 1000;
        //???????????? ??????**
        if (refreshToken3.isPresent()) {
            RedisRefreshToken savedRefresh = refreshToken3.get().updateToken(tokenDto.getRefreshToken(), expiration);
            redisRepository.save(savedRefresh);
        } else {
            RedisRefreshToken refreshToSave = new RedisRefreshToken(kakaoUser.getEmail(), tokenDto.getRefreshToken(), expiration);
            redisRepository.save(refreshToSave);
        }

        //????????? header??? ????????? ????????????????????? ????????????
        setHeader(response, tokenDto);

        List<String> bookmarkList = new ArrayList<>();
        List<Bookmark> bookmarks = bookmarkRepository.findBookmarksByAccountId(kakaoUser.getId());
        for (Bookmark bookmark : bookmarks) {
            bookmarkList.add(bookmark.getGu().getGuName());
        }

        return GlobalResponseDto.ok(kakaoUserInfo.getNickname() + message, new LoginResponseDto(kakaoUser, bookmarkList));
    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }

    private String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header ??????
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body ??????
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoApiKey); //Rest API ???
        body.add("redirect_uri", "https://boombiboombi.com/user/kakao/callback");
        body.add("code", code);

        // HTTP ?????? ?????????
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP ?????? (JSON) -> ????????? ?????? ??????
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header ??????
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP ?????? ?????????
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
        String profileImage = jsonNode.get("kakao_account")
                .get("profile").get("profile_image_url").asText();
        JsonNode gender = jsonNode.get("kakao_account");
        String genderStr = gender.hasNonNull("gender") ? gender.get("gender").asText() : "?????????";
        JsonNode ageRange = jsonNode.get("kakao_account");
        String ageRangeStr = ageRange.hasNonNull("age_range") ? ageRange.get("age_range").asText() : "?????????";
        return new KakaoUserInfoDto(id, nickname, email, profileImage, genderStr, ageRangeStr);
    }


    private Account registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        // DB ??? ????????? Kakao Id ??? ????????? ??????
        Long kakaoId = kakaoUserInfo.getId();
        Account kakaoUser = accountRepository.findByKakaoId(kakaoId)
                .orElse(null);
        if (kakaoUser == null) {
            // ????????? ????????? ???????????? ????????? ???????????? ?????? ????????? ????????? ??????
            String kakaoEmail = kakaoUserInfo.getEmail();
            Account sameEmailUser = accountRepository.findByEmail(kakaoEmail).orElse(null);
            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser;
                // ?????? ??????????????? ????????? Id ??????
                kakaoUser.setKakaoId(kakaoId);
            } else {
                // ?????? ????????????
                // username: kakao nickname
                String nickname = kakaoUserInfo.getNickname();
                int count = accountRepository.countByAccountName(nickname);
                nickname = count == 0 ? nickname : nickname + count;

                // password: random UUID
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                // email: kakao email
                String email = kakaoUserInfo.getEmail();

                // ????????? ?????? ????????????
                String profileImage = kakaoUserInfo.getProfileImage();
                // ?????? ????????????
                String gender = kakaoUserInfo.getGender();
                // ????????? ????????????
                String ageRange = kakaoUserInfo.getAgeRange();
                kakaoUser = new Account(nickname, encodedPassword, email, profileImage, kakaoId, gender, ageRange);
                // ??????????????? ??????
                kakaoUser.setMyPage(new MyPage(kakaoUser));

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

    // logout
    @Transactional
    public GlobalResponseDto<String> logout(Account account) {

        Optional<RedisRefreshToken> redisEntity = redisRepository.findById(account.getEmail());
        if (redisEntity.isPresent()) {
            redisRepository.deleteById(account.getEmail());
        }
        return GlobalResponseDto.ok("Success Logout", null);
    }

    // ????????? ????????????
    @Transactional
    public Integer kakaoSignout(Account account) throws JsonProcessingException {

        // HTTP Header ??????
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded");
        headers.add("Authorization", "KakaoAK " + kakaoAdminKey);

        // HTTP Body ??????
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("target_id_type", "user_id");
        body.add("target_id", String.valueOf(account.getKakaoId())); //Rest API ???

        // HTTP ?????? ?????????
        HttpEntity<MultiValueMap<String, String>> kakaoSignoutRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v1/user/unlink",
                HttpMethod.POST,
                kakaoSignoutRequest,
                String.class
        );

        // HTTP ?????? (JSON) -> ????????? ?????? ??????
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String responseId = jsonNode.get("id").asText();

        Account account1 = accountRepository.findById(account.getId()).orElseThrow(
                ()-> new CustomException(ErrorCode.NotFoundUser));

        if(account.getKakaoId().toString().equals(responseId)){
            return 1;
        } else {
            throw new CustomException(ErrorCode.FailKakaoSignout);
        }

    }
}