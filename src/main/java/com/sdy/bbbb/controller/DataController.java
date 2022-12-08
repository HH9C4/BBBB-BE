package com.sdy.bbbb.controller;

import com.sdy.bbbb.config.UserDetailsImpl;
import com.sdy.bbbb.dto.response.data.BaseGuInfoDto;
import com.sdy.bbbb.dto.response.data.DataResponseDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import com.sdy.bbbb.service.data.DataService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DataController {

    private final DataService dataService;

    // 데이터 호출 api
    @GetMapping("/api/maininfo")
    @ApiOperation(value = "메인페이지 정보", notes = "내용입니다")
    public GlobalResponseDto<DataResponseDto> getMainInfo() {
        return dataService.getMainInfo();
    }

    @GetMapping("/api/guinfo")
    @ApiOperation(value = "구 별 정보", notes = "내용입니다")
    public GlobalResponseDto<BaseGuInfoDto> getGuInformation(@RequestParam String gu) {
        return dataService.getGuInformation(gu);
    }

    @GetMapping("/api/guinfo2")
    @ApiOperation(value = "구 별 정보", notes = "내용입니다")
    public GlobalResponseDto<BaseGuInfoDto> getGuInformation2(@RequestParam String gu,
                                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return dataService.getGuInformation2(gu, userDetails.getAccount());
    }

}
