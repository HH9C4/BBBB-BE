package com.sdy.bbbb.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class RedisCont {

    private final RedisTest redisTest;
    private final RedisRepository redisRepository;

    @GetMapping("/test")
    public void test(){
        Optional<RedisEntity> asd = redisRepository.findById("anfrosus@naver.com");
        System.out.println(asd.get().getRefreshToken());


    }

    @PostMapping("/test")
    public void test1(){
        RedisEntity redisEntity = new RedisEntity("anfrosus@naver.com", "token2da", 30L);
        redisRepository.save(redisEntity);

    }

}
