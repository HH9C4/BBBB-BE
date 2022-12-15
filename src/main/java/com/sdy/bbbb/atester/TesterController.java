package com.sdy.bbbb.atester;

import com.sdy.bbbb.dto.request.ChattingDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.dto.response.LoginResponseDto;
import com.sdy.bbbb.redis.RedisData;
import com.sdy.bbbb.redis.RedisDataRepository;
import com.sdy.bbbb.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class TesterController {

    private final RedisDataRepository redisDataRepository;

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
    public String test() {
        ChattingDto chattingDto = new ChattingDto();
        chattingDto.setMessage("테스트입니다");
        chattingDto.setSender("테스트라구용");
        RedisData redisData = new RedisData("test1", chattingDto);
        redisDataRepository.save(redisData);
        return "hi";
    }

}
