package com.sdy.bbbb.data;

import com.sdy.bbbb.data.dataDto.BaseGuInfoDto;
import com.sdy.bbbb.data.dataDto.DataResponseDto;
import com.sdy.bbbb.data.dataDto.PopulationChangesDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DataController {

    private final DataService dataService;

    // 데이터 1번 주말데이터 저장 (테스트용)
//    @GetMapping("/api/weekend")
//    public void saveWeekendData() {
//        dataService.saveWeekendData();
//    }

    // 데이터 1번 주중데이터 저장 (테스트용)
//    @GetMapping("/api/weekday")
//    public void saveWeekdayData() {
//        dataService.saveWeekdayData();
//    }

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


}
