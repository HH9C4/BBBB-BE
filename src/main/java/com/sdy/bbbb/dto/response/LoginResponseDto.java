package com.sdy.bbbb.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sdy.bbbb.entity.Account;

import com.sdy.bbbb.entity.Bookmark;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
public class LoginResponseDto {

    private String accountName;

    private String ageRange;

    private String email;

    private String gender;

    private String profileImage;

    private boolean naverUser;

    private boolean kakaoUser;

    private int reportedCount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> bookmarkList;


    public LoginResponseDto(Account account, List<String> bookmarkList) {
        this.accountName = account.getAccountName();
        this.ageRange = account.getAgeRange();
        this.email = account.getEmail();
        this.gender = account.getGender();
        this.profileImage = account.getProfileImage();
        this.naverUser = account.getNaverId() != null;
        this.kakaoUser = account.getKakaoId() != null;
        this.reportedCount = account.getReportedCount();
        this.bookmarkList = bookmarkList;
    }

    public LoginResponseDto(Account account) {
        this.accountName = account.getAccountName();
        this.ageRange = account.getAgeRange();
        this.email = account.getEmail();
        this.gender = account.getGender();
        this.profileImage = account.getProfileImage();
        this.naverUser = account.getNaverId() != null;
        this.kakaoUser = account.getKakaoId() != null;
        this.reportedCount = account.getReportedCount();
    }

}
