package com.sdy.bbbb.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenDto {

    private String accessToken;
    private String refreshToken;

    public TokenDto(String accessToken, String refreshToken) {
        this.accessToken = "bearer " + accessToken;
        this.refreshToken = "bearer " + refreshToken;
    }

}
