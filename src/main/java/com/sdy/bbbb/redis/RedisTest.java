//package com.sdy.bbbb.redis;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//import java.util.concurrent.TimeUnit;
//
//@RequiredArgsConstructor
//@Service
//public class RedisTest {
//
////    @Autowired
//    private final RedisTemplate<String, String> redisTemplate;
//
//    public void test(){
//        String key = "a";
//        String data = "data1";
//
//        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//        valueOperations.set(key, data, 7, TimeUnit.DAYS);
//        //토큰 -> 만료시간 -> now() -> 초로 환산해 -> eamil + 만료시간 +
//        LocalDateTime day = LocalDateTime.of(2022, 11, 28, 23, 00);
//        long gap = ChronoUnit.MICROS.between(day, LocalDateTime.now());
////        ChronoUnit.MILLIS.between()
//    }
//
//}
