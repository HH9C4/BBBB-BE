package com.sdy.bbbb.data;

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

    @GetMapping("/api/test")
    public void callApi() throws Exception {
        dataService.call();
    }

    @GetMapping("/api/population")
    @ApiOperation(value = "정보2번 실시간 혼잡도", notes = "내용입니다")
    public GlobalResponseDto<List<PopulationChangesDto>> getPopulation() {
        return dataService.getPopulationChanges();
    }

}
