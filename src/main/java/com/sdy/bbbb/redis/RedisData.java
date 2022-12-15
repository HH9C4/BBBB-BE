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
    private String ranking;
    private String area_nm;
    private String gu_nm;
    private Boolean isWeekend;
    private String saved_week;
    private Long sumOfScore;
    private ChattingDto testObj;

    public RedisData(String id, List<ChattingDto> obj) {
        this.id = id;
        this.obj = obj;
    }

}
