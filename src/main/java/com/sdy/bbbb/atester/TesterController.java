package com.sdy.bbbb.atester;

import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.dto.response.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class TesterController {

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
        return "hi";
    }

}
