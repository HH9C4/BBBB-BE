package com.sdy.bbbb.data;

import com.sdy.bbbb.data.dataDto.DataResponseDto;
import com.sdy.bbbb.data.dataDto.PopulationChangesDto;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DataController {

    private final DataService dataService;

    // 데이터 1번 주말데이터 저장
    @GetMapping("/api/weekend")
    public void saveWeekendData() {
        dataService.saveWeekendData();
    }

    // 데이터 1번 주중데이터 저장
    @GetMapping("/api/weekday")
    public void saveWeekdayData() {
        dataService.saveWeekdayData();
    }

    // 데이터 호출 api
    @GetMapping("/api/population")
    @ApiOperation(value = "정보2번 실시간 혼잡도", notes = "내용입니다")
    public GlobalResponseDto<DataResponseDto> getPopulation() {
        return dataService.getPopulationChanges();
    }


}
