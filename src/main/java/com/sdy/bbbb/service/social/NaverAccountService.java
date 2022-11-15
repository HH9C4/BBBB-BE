package com.sdy.bbbb.service.social;

import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.request.loginRequestDto.NaverUserInfoDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.dto.response.LoginResponseDto;
import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.MyPage;
import com.sdy.bbbb.entity.RefreshToken;
import com.sdy.bbbb.jwt.JwtUtil;
import com.sdy.bbbb.jwt.TokenDto;
import com.sdy.bbbb.repository.AccountRepository;
import com.sdy.bbbb.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    //네이버 로그인 로직
    @Transactional
    public GlobalResponseDto<LoginResponseDto> naverLogin(String code, String state, HttpServletResponse response) throws IOException {
        // 네이버에서 code와 state로 가져온 유저정보
        NaverUserInfoDto naverUser = getNaverUserInfo(code, state);
        // 재가입 방지
        // 네이버 ID로 유저 정보 DB 에서 조회
        String naverId = naverUser.getSocialId();
        Account naverAccount = accountRepository.findByNaverId(naverId).orElse(null);
        // (1) 네이버 아이디로 db조회했을 때 null이라면
        if (naverAccount == null) {
            //(1-1) 네이버에서 받아온 이메일로 account를 찾았을 때
            String naverEmail = naverUser.getUserEmail();
            Account sameEmailUser = accountRepository.findByEmail(naverEmail).orElse(null);
            //(1-1-1) null이 아니라면
            if (sameEmailUser != null) {
                naverAccount = sameEmailUser;
                naverAccount.setNaverId(naverId);
            } else { //(1-1-2) null이라면
                // 신규 회원가입
                // nickname(accountName) = naverUser의 nickname
                String nickname = naverUser.getNickname();

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
                naverAccount = new Account(nickname, encodedPassword, profileImage, email, naverId, gender, ageRange);

                //마이페이지 생성
                naverAccount.setMyPage(new MyPage(naverAccount));

            }
            accountRepository.save(naverAccount);
        }

        // 강제 로그인
        forceLogin(naverAccount);

        //토큰 발급
        TokenDto tokenDto = jwtUtil.createAllToken(naverAccount.getEmail());

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail(naverAccount.getEmail());

        // 로그아웃한 후 로그인을 다시 하는가?
        if (refreshToken.isPresent()) {
            RefreshToken refreshToken1 = refreshToken.get().updateToken(tokenDto.getRefreshToken());
            refreshTokenRepository.save(refreshToken1);
        } else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), naverAccount.getEmail());
            refreshTokenRepository.save(newToken);
        }

        //토큰을 header에 넣어서 클라이언트에게 전달하기
        setHeader(response, tokenDto);

        return GlobalResponseDto.ok(naverAccount.getAccountName() + "님 환영합니다.", new LoginResponseDto(naverAccount));
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
                    "&redirect_uri=http://localhost:3000/user/signin/naver" +
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
                .getAsJsonObject().get("name"));
        String profileImage = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("profile_image"));
        String age = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("age"));
        String gender = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("gender"));

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

}


