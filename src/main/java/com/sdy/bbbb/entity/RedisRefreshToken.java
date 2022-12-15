package com.sdy.bbbb.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@RedisHash("refresh")
public class RedisRefreshToken {
    @Id
    private String email;
    private String refreshToken;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long expiration;

    public RedisRefreshToken(String email, String refreshToken, Long expiration) {
        this.email = email;
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }

    public RedisRefreshToken updateToken(String refreshToken, Long expiration){
        this.refreshToken = refreshToken;
        this.expiration = expiration;
        return this;
    }
}
