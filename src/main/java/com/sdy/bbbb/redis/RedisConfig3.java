//package com.sdy.bbbb.redis;
//
//import io.lettuce.core.ConnectionPoint;
//import io.lettuce.core.RedisURI;
//import io.lettuce.core.*;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.cache.CacheProperties;
//
//import java.io.Serializable;
//
//public class RedisConfig3 extends Object implements Serializable, ConnectionPoint {
//
//    @Override
//    public String getHost() {
//        return null;
//    }
//
//    @Override
//    public int getPort() {
//        return 0;
//    }
//
//    @Override
//    public String getSocket() {
//        return null;
//    }
//
//    @Value("${spring.redis.host}")
//    private String redisHost;
//
//    @Value("${spring.redis.port}")
//    private int redisPort;
//
//    @Value("${spring.redis.password}")
//    private String password;
//
////   RedisURI uri = new RedisURI();
//
//
//}
////        RedisURI
////
////        RedisURI.create("redis://localhost/");
////
////        See create(String) for more options
////        Use the Builder:
////        RedisURI.Builder.redis("localhost", 6379).withPassword("password").withDatabase(1).build();
////
////        See RedisURI.Builder.redis(String) and RedisURI.Builder.sentinel(String) for more options.
////        Construct your own instance:
////        new RedisURI("localhost", 6379, Duration.ofSeconds(60));
////
////        or
////        RedisURI uri = new RedisURI(); uri.setHost("localhost");
