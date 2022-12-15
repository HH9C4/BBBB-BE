package com.sdy.bbbb.redis;

import com.sdy.bbbb.dto.request.ChattingDto;
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
    private List<ChattingDto> obj;

    public RedisData(String id, List<ChattingDto> obj) {
        this.id = id;
        this.obj = obj;
    }

}
