package com.sdy.bbbb.dto.response;

import com.sdy.bbbb.entity.Account;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class LoginResponseDto {

    private String accountName;

    private String ageRange;

    private String email;

    private String gender;

    private String profileImage;


    public LoginResponseDto(Account kakaoUser) {
        this.accountName = kakaoUser.getAccountName();
        this.ageRange = kakaoUser.getAgeRange();
        this.email = kakaoUser.getEmail();
        this.gender = kakaoUser.getGender();
        this.profileImage = kakaoUser.getProfileImage();
    }
}
