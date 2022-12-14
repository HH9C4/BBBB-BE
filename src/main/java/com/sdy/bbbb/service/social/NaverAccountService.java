package com.sdy.bbbb.service.social;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.request.loginRequestDto.NaverUserInfoDto;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    //????????? ????????? ??????
    @Transactional
    public GlobalResponseDto<LoginResponseDto> naverLogin(String code, String state, HttpServletResponse response) throws IOException {

        String message = "??? ???????????????.";
        // ??????????????? code??? state??? ????????? ????????????
        NaverUserInfoDto naverUser = getNaverUserInfo(code, state);
        // ????????? ??????
        // ????????? ID??? ?????? ?????? DB ?????? ??????
        String naverId = naverUser.getSocialId();
        String naverRefreshToken = naverUser.getRefreshToken();
        Account naverAccount = accountRepository.findByNaverId(naverId).orElse(null);
        // (1) ????????? ???????????? db???????????? ??? null?????????
        if (naverAccount == null) {
            //(1-1) ??????????????? ????????? ???????????? account??? ????????? ???
            String naverEmail = naverUser.getUserEmail();
            Account sameEmailUser = accountRepository.findByEmail(naverEmail).orElse(null);
            //(1-1-1) null??? ????????????(???????????? ?????? ????????? ??????)
            if (sameEmailUser != null) {
                naverAccount = sameEmailUser;
                naverAccount.setNaverId(naverId);
                naverAccount.setNaverRefreshToken(naverRefreshToken);
                message += " ????????? ???????????? ????????? ????????? ?????????????????????!";
            } else { //(1-1-2) null?????????
                // ?????? ????????????
                // nickname(accountName) = naverUser??? nickname
                String nickname = naverUser.getNickname();
                int count = accountRepository.countByAccountName(nickname);
                nickname = count == 0 ? nickname : nickname + count;

                // ?????????????????? ??????
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                // ????????? ??????
                String profileImage = naverUser.getProfileImage();

                // email : naver email
                String email = naverUser.getUserEmail();

                // ??????
                String gender = naverUser.getGender();

                // ?????????
                String ageRange = naverUser.getAge();

                // ????????????
                naverAccount = new Account(nickname, encodedPassword, email, profileImage, naverId, naverRefreshToken, gender, ageRange);

                //??????????????? ??????
                naverAccount.setMyPage(new MyPage(naverAccount));

            }
            accountRepository.save(naverAccount);
        } else {
            naverAccount.setNaverRefreshToken(naverRefreshToken);
        }

        // ?????? ?????????
        forceLogin(naverAccount);

        //?????? ??????
        TokenDto tokenDto = jwtUtil.createAllToken(naverAccount.getEmail());

        //??????????????? ???????????? ????????????
        Optional<RedisRefreshToken> refreshToken3 = redisRepository.findById(naverAccount.getEmail());
        long expiration = JwtUtil.REFRESH_TIME / 1000;
        //???????????? ??????**
        if (refreshToken3.isPresent()) {
            RedisRefreshToken savedRefresh = refreshToken3.get().updateToken(tokenDto.getRefreshToken(), expiration);
            redisRepository.save(savedRefresh);
        } else {
            RedisRefreshToken refreshToSave = new RedisRefreshToken(naverAccount.getEmail(), tokenDto.getRefreshToken(), expiration);
            redisRepository.save(refreshToSave);
        }


        //????????? header??? ????????? ????????????????????? ????????????
        setHeader(response, tokenDto);

        List<String> bookmarkList = new ArrayList<>();
        List<Bookmark> bookmarks = bookmarkRepository.findBookmarksByAccountId(naverAccount.getId());
        for (Bookmark bookmark : bookmarks) {
            bookmarkList.add(bookmark.getGu().getGuName());
        }

        return GlobalResponseDto.ok(naverAccount.getAccountName() + message, new LoginResponseDto(naverAccount, bookmarkList));
    }


    // ???????????? ???????????? ????????? ?????? ?????? ?????????
    public JsonElement jsonElement(String reqURL, String token, String code, String state) throws IOException {

        // ???????????? URL ??????
        URL url = new URL(reqURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // POST ????????? ?????? ???????????? false??? setDoOutput??? true???
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        // POST ????????? ????????? ????????? ?????? ??? ??????
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

        // ????????? ?????? ?????? JSON????????? Response ????????? ????????????
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder result = new StringBuilder();

        while ((line = br.readLine()) != null) {
            result.append(line);
        }
        br.close();

        // Gson ?????????????????? ????????? ???????????? JSON ??????
        return JsonParser.parseString(result.toString());
    }


    // ???????????? ???????????? ???????????? ?????? ?????????
    public NaverUserInfoDto getNaverUserInfo(String code, String state) throws IOException {

        String codeReqURL = "https://nid.naver.com/oauth2.0/token";
        String tokenReqURL = "https://openapi.naver.com/v1/nid/me";


        // ????????? ???????????? ???????????? ????????? ?????? ?????????
        JsonElement tokenElement = jsonElement(codeReqURL, null, code, state);

        String access_Token = tokenElement.getAsJsonObject().get("access_token").getAsString();
        String refresh_token = tokenElement.getAsJsonObject().get("refresh_token").getAsString();

        // ????????? ????????? ???????????? ???????????? ???????????? ?????????
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

    // ????????? ?????? ??????
    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }

    // ?????? ????????? ??????
    private void forceLogin(Account naverAccount) {
        UserDetails userDetails = new UserDetailsImpl(naverAccount);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // ????????? ????????????
    public Integer naverSignout(Account account) throws IOException {

        String reqUrl = "https://nid.naver.com/oauth2.0/token";
        JsonElement reissued = reissueOrDelete(reqUrl, account.getNaverRefreshToken(), "reissue");
        String access_Token = reissued.getAsJsonObject().get("access_token").getAsString();
        JsonElement deleteReq = reissueOrDelete(reqUrl, access_Token, "delete");
        String result = deleteReq.getAsJsonObject().get("result").getAsString();

        if(result.equals("success")){
            return 1;
        } else {
            throw new CustomException(ErrorCode.FailKakaoSignout);
        }

    }

    public JsonElement reissueOrDelete(String reqURL, String token, String type) throws IOException {

        // ???????????? URL ??????
        URL url = new URL(reqURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // POST ????????? ?????? ???????????? false??? setDoOutput??? true???
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        // POST ????????? ????????? ????????? ?????? ??? ??????
            if(type.equals("reissue")) {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
                String sb = "grant_type=refresh_token" +
                        "&client_id=" + clientId +
                        "&client_secret=" + clientSecret +
                        "&refresh_token=" + token;
                bw.write(sb);
                bw.flush();
                bw.close();
            }else{
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
                String sb = "grant_type=delete" +
                        "&client_id=" + clientId +
                        "&client_secret=" + clientSecret +
                        "&access_token=" + token;
                bw.write(sb);
                bw.flush();
                bw.close();
            }

        // ????????? ?????? ?????? JSON????????? Response ????????? ????????????
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder result = new StringBuilder();

        while ((line = br.readLine()) != null) {
            result.append(line);
        }
        br.close();

        // Gson ?????????????????? ????????? ???????????? JSON ??????
        return JsonParser.parseString(result.toString());
    }
}


