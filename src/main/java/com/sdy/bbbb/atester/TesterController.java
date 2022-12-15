package com.sdy.bbbb.atester;

import com.sdy.bbbb.dto.request.ChattingDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.dto.response.LoginResponseDto;
import com.sdy.bbbb.dto.response.data.JamDto;
import com.sdy.bbbb.dto.response.data.JamTop5Dto;
import com.sdy.bbbb.entity.data.JamOfWeek;
import com.sdy.bbbb.redis.RedisData;
import com.sdy.bbbb.redis.RedisDataRepository;
import com.sdy.bbbb.redis.RedisRepository;
import com.sdy.bbbb.repository.data.DataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TesterController {

    private final RedisDataRepository redisDataRepository;
    private final DataRepository dataRepository;

    private final TestService testService;
    @GetMapping("/user/tester")
    public GlobalResponseDto<LoginResponseDto> login(HttpServletResponse response) {
        return testService.login(response);
    }

    @GetMapping("/user/tester2")
    public GlobalResponseDto<LoginResponseDto> login2(HttpServletResponse response) {
        return testService.login2(response);
    }

    @GetMapping("/coffee")
    public RedisData test() {
        List<JamTop5Dto> jamTop5Dtos = new ArrayList<>();
        List<JamDto> jamDtos = dataRepository.getJamWeekDayFromDb();
        Long i = 1L;
        for (JamDto jamDto : jamDtos){
            jamTop5Dtos.add(new JamTop5Dto(i++, jamDto, false));
        }
        RedisData redisData = new RedisData("weekday", jamTop5Dtos);
        redisDataRepository.save(redisData);
        RedisData asdf = redisDataRepository.findById("weekday").get();

        List<JamTop5Dto> jamTop5Dtos2 = new ArrayList<>();
        List<JamDto> jamDtos2 = dataRepository.getJamWeekendFromDb();
        Long j = 1L;
        for (JamDto jamDto : jamDtos2){
            jamTop5Dtos2.add(new JamTop5Dto(j++, jamDto, true));
        }
        RedisData redisData2 = new RedisData("weekend", jamTop5Dtos);
        redisDataRepository.save(redisData2);
        RedisData asdf1 = redisDataRepository.findById("weekend").get();
        return asdf1;
    }

}
