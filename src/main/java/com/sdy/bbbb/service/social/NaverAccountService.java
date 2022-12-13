package com.sdy.bbbb.service.social;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.request.loginRequestDto.NaverUserInfoDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.dto.response.LoginResponseDto;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.Bookmark;
import com.sdy.bbbb.entity.MyPage;
import com.sdy.bbbb.jwt.JwtUtil;
import com.sdy.bbbb.jwt.TokenDto;
import com.sdy.bbbb.redis.RedisEntity;
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
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NaverAccountService {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisRepository redisRepository;
    private final BookmarkRepository bookmarkRepository;

    //네이버 로그인 로직
    @Transactional
    public GlobalResponseDto<LoginResponseDto> naverLogin(String code, String state, HttpServletResponse response) throws IOException {

        String message = "님 환영합니다.";
        // 네이버에서 code와 state로 가져온 유저정보
        NaverUserInfoDto naverUser = getNaverUserInfo(code, state);
        // 재가입 방지
        // 네이버 ID로 유저 정보 DB 에서 조회
        String naverId = naverUser.getSocialId();
        String naverRefreshToken = naverUser.getRefreshToken();
        Account naverAccount = accountRepository.findByNaverId(naverId).orElse(null);
        // (1) 네이버 아이디로 db조회했을 때 null이라면
        if (naverAccount == null) {
            //(1-1) 네이버에서 받아온 이메일로 account를 찾았을 때
            String naverEmail = naverUser.getUserEmail();
            Account sameEmailUser = accountRepository.findByEmail(naverEmail).orElse(null);
            //(1-1-1) null이 아니라면(카카오로 이미 가입한 유저)
            if (sameEmailUser != null) {
                naverAccount = sameEmailUser;
                naverAccount.setNaverId(naverId);
                naverAccount.setNaverRefreshToken(naverRefreshToken);
                message += " 기존에 가입하신 카카오 계정과 연동되었습니다!";
            } else { //(1-1-2) null이라면
                // 신규 회원가입
                // nickname(accountName) = naverUser의 nickname
                String nickname = naverUser.getNickname();
                int count = accountRepository.countByAccountName(nickname);
                nickname = count == 0 ? nickname : nickname + count;

                // 임의비밀번호 생성
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                // 프로필 사진
                String profileImage = naverUser.getProfileImage();

                // email : naver email
                String email = naverUser.getUserEmail();

                // 성별
                String gender = naverUser.getGender();

                // 나이대
                String ageRange = naverUser.getAge();

                // 회원가입
                naverAccount = new Account(nickname, encodedPassword, email, profileImage, naverId, naverRefreshToken, gender, ageRange);

                //마이페이지 생성
                naverAccount.setMyPage(new MyPage(naverAccount));

            }
            accountRepository.save(naverAccount);
        } else {
            naverAccount.setNaverRefreshToken(naverRefreshToken);
        }

        // 강제 로그인
        forceLogin(naverAccount);

        //토큰 발급
        TokenDto tokenDto = jwtUtil.createAllToken(naverAccount.getEmail());

        //레디스에서 옵셔널로 받아오기
        Optional<RedisEntity> refreshToken3 = redisRepository.findById(naverAccount.getEmail());
        long expiration = JwtUtil.REFRESH_TIME / 1000;
        //레디스의 영역**
        if (refreshToken3.isPresent()) {
            RedisEntity savedRefresh = refreshToken3.get().updateToken(tokenDto.getRefreshToken(), expiration);
            redisRepository.save(savedRefresh);
        } else {
            RedisEntity refreshToSave = new RedisEntity(naverAccount.getEmail(), tokenDto.getRefreshToken(), expiration);
            redisRepository.save(refreshToSave);
        }


        //토큰을 header에 넣어서 클라이언트에게 전달하기
        setHeader(response, tokenDto);

        List<String> bookmarkList = new ArrayList<>();
        List<Bookmark> bookmarks = bookmarkRepository.findBookmarksByAccountId(naverAccount.getId());
        for (Bookmark bookmark : bookmarks) {
            bookmarkList.add(bookmark.getGu().getGuName());
        }

        return GlobalResponseDto.ok(naverAccount.getAccountName() + message, new LoginResponseDto(naverAccount, bookmarkList));
    }


    // 네이버에 요청해서 데이터 전달 받는 메소드
    public JsonElement jsonElement(String reqURL, String token, String code, String state) throws IOException {

        // 요청하는 URL 설정
        URL url = new URL(reqURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // POST 요청을 위해 기본값이 false인 setDoOutput을 true로
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        // POST 요청에 필요한 데이터 저장 후 전송
        if (token == null) {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id=" + clientId +
                    "&client_secret=" + clientSecret +
                    "&redirect_uri= https://boombiboombi.com/user/signin/naver" +
                    "&code=" + code +
                    "&state=" + state;
            bw.write(sb);
            bw.flush();
            bw.close();
        } else {
            conn.setRequestProperty("Authorization", "Bearer " + token);
        }

        // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder result = new StringBuilder();

        while ((line = br.readLine()) != null) {
            result.append(line);
        }
        br.close();

        // Gson 라이브러리에 포함된 클래스로 JSON 파싱
        return JsonParser.parseString(result.toString());
    }


    // 네이버에 요청해서 회원정보 받는 메소드
    public NaverUserInfoDto getNaverUserInfo(String code, String state) throws IOException {

        String codeReqURL = "https://nid.naver.com/oauth2.0/token";
        String tokenReqURL = "https://openapi.naver.com/v1/nid/me";


        // 코드를 네이버에 전달하여 엑세스 토큰 가져옴
        JsonElement tokenElement = jsonElement(codeReqURL, null, code, state);

        String access_Token = tokenElement.getAsJsonObject().get("access_token").getAsString();
        String refresh_token = tokenElement.getAsJsonObject().get("refresh_token").getAsString();

        // 엑세스 토큰을 네이버에 전달하여 유저정보 가져옴
        JsonElement userInfoElement = jsonElement(tokenReqURL, access_Token, null, null);

        String naverId = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("id"));
        String userEmail = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("email"));
        String nickName = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("nickname"));
        String profileImage = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("profile_image"));
        String age = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("age"));
        String gender = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("gender"));

        naverId = naverId.substring(1, naverId.length() - 1);
        userEmail = userEmail.substring(1, userEmail.length() - 1);
        nickName = nickName.substring(1, nickName.length() - 1);
        profileImage = profileImage.substring(1, profileImage.length() - 1);
        age = age.substring(1, age.length() - 1).replace('-', '~');
        gender = gender.substring(1, gender.length() - 1).equals("M") ? "male" : "female";

        return new NaverUserInfoDto(naverId, nickName, userEmail, profileImage, age, gender, access_Token, refresh_token);
    }

    // 헤더에 토큰 생성
    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }

    // 강제 로그인 처리
    private void forceLogin(Account naverAccount) {
        UserDetails userDetails = new UserDetailsImpl(naverAccount);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // 네이버 연결끊기
    public GlobalResponseDto<?> naverSignout(Account account) throws JsonProcessingException {
        // 1. 리프레시 토큰으로 엑세스 토큰 재발급
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("client_id", clientId);
        headers.add("client_secret", clientSecret);
        headers.add("refresh_token", account.getNaverRefreshToken());
        headers.add("grant_type", "refresh_token");
        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> naverAccessTokenReissue =
                new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                naverAccessTokenReissue,
                String.class
        );
        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String responseAccessToken = jsonNode.get("access_token").asText();
        String responseTokenType = jsonNode.get("token_type").asText();
        String responseExpires = jsonNode.get("expires_in").asText();


        // 2. 재발급 받은 엑세스 토큰으로 네이버 연결끊기 요청
        // HTTP Header 생성
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("client_id", clientId);
        headers2.add("client_secret", clientSecret);
        headers2.add("access_token", responseAccessToken);
        headers2.add("grant_type", "delete");
        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> naverSignout =
                new HttpEntity<>(headers2);
        RestTemplate rt2 = new RestTemplate();
        ResponseEntity<String> response2 = rt.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                naverSignout,
                String.class
        );
        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody2 = response2.getBody();
        ObjectMapper objectMapper2 = new ObjectMapper();
        JsonNode jsonNode2 = objectMapper2.readTree(responseBody2);
        String responseAccessToken2 = jsonNode2.get("access_token").asText();
        String result = jsonNode2.get("result").asText();


        // 3. 카카오연동 되어 있는지 확인? 있으면 같이 탈퇴 처리


        // 4. DB에서 정보 바꿔주기
        if(result.equals("success")){
            account.signOut();
        } else {
            return GlobalResponseDto.fail("탈퇴 실패");
        }

        return GlobalResponseDto.ok("탈퇴완료", null);
    }
}


