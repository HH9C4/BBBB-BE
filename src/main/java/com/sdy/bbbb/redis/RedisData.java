package com.sdy.bbbb.redis;

import com.sdy.bbbb.dto.request.ChattingDto;
import com.sdy.bbbb.dto.response.data.JamTop5Dto;
import com.sdy.bbbb.entity.data.JamOfWeek;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter
@RedisHash("data")
public class RedisData {

    @Id
    private String id;
    private List<JamTop5Dto> jamTop5DtoList;

    public RedisData(String id, List<JamTop5Dto> jamTop5DtoList) {
        this.id = id;
        this.jamTop5DtoList = jamTop5DtoList;
    }

}
