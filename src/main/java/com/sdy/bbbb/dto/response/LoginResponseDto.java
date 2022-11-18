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


    public LoginResponseDto(Account account) {
        this.accountName = account.getAccountName();
        this.ageRange = account.getAgeRange();
        this.email = account.getEmail();
        this.gender = account.getGender();
        this.profileImage = account.getProfileImage();
    }
}
