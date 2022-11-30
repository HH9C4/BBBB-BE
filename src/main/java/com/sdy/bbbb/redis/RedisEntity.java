package com.sdy.bbbb.redis;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash("refresh")
public class RedisEntity {
    @Id
    private String email;
    private String refreshToken;

    @TimeToLive
    private Long expiration;

    public RedisEntity(String email, String refreshToken, Long expiration) {
        this.email = email;
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }

    public RedisEntity updateToken(String refreshToken, Long expiration){
        this.refreshToken = refreshToken;
        this.expiration = expiration;
        return this;
    }
}
