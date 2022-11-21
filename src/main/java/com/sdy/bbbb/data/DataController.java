package com.sdy.bbbb.data;

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

//    @GetMapping("/api/test")
//    public void callApi() throws Exception {
//        dataService.call();
//    }


    // 데이터 1번 호출
    @GetMapping("/api/jam")
    @ApiOperation(value = "정보1번 지난 주 가장 혼잡했던 spot", notes = "내용입니다")
    public GlobalResponseDto<?> getJam() {
        return dataService.getJam();
    }

    // 데이터 2번 호출
    @GetMapping("/api/population")
    @ApiOperation(value = "정보2번 실시간 혼잡도", notes = "내용입니다")
    public GlobalResponseDto<List<PopulationChangesDto>> getPopulation() {
        return dataService.getPopulationChanges();
    }


}
