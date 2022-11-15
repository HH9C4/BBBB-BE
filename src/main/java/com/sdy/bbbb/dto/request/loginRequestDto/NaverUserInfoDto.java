package com.sdy.bbbb.dto.request.loginRequestDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class NaverUserInfoDto {

    private String socialId;

    private String nickname;

    private String userEmail;

    private String profileImage;

    private String age;

    private String gender;

    private String accessToken;

    private String refreshToken;

    @Builder
    public NaverUserInfoDto(String naverId, String nickname, String userEmail, String profileImage, String  age, String gender, String accessToken,  String refreshToken) {
        this.socialId = naverId;
        this.nickname = nickname;
        this.userEmail = userEmail;
        this.profileImage = profileImage;
        this.age = age;
        this.gender = gender;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
