package com.sdy.bbbb.data;

import com.sdy.bbbb.data.dataDto.JamDto;
import com.sdy.bbbb.data.dataDto.JamTop5Dto;
import com.sdy.bbbb.data.dataDto.PopulationChangesDto;
import com.sdy.bbbb.data.dataDto.PopulationDto;
import com.sdy.bbbb.data.entity.JamOfWeek;
import com.sdy.bbbb.dto.response.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataService {

    private final DataRepository dataRepository;
    private final JamRepository jamRepository;

    // 데이터 1번 조회 (점수 매기기)
    @Transactional(readOnly = true)
    public GlobalResponseDto<?> getJam() {
        List<JamDto> jamDtos = dataRepository.getJamWeekendFromDb();
        List<JamTop5Dto> jamTop5Dtos = new ArrayList<>();
        for(JamDto jam : jamDtos) {
            jamTop5Dtos.add(new JamTop5Dto(jam));
        }
        return GlobalResponseDto.ok("조회 성공", jamTop5Dtos);
    }

    // 데이터 1 - 주말 데이터 저장 로직
    @Transactional(readOnly = true)
    public GlobalResponseDto<?> test() {
        List<JamDto> jamDtos = dataRepository.getJamWeekendFromDb();
        List<JamTop5Dto> jamTop5Dtos = new ArrayList<>();
        List<JamOfWeek> jamOfWeekList = new ArrayList<>();
        for(JamDto jam : jamDtos) {
            jamTop5Dtos.add(new JamTop5Dto(jam));
            jamOfWeekList.add(new JamOfWeek(jam, true));
        }

        jamRepository.saveAll(jamOfWeekList);

        return GlobalResponseDto.ok("저장 성공", jamTop5Dtos);
    }


    // 데이터 2번 조회 (인구수 +-)
    @Transactional(readOnly = true)
    public GlobalResponseDto<List<PopulationChangesDto>> getPopulationChanges() {
        List<PopulationDto> popList = dataRepository.getPopulationFromDb();
        List<PopulationChangesDto> dtoList = new ArrayList<>();
        for (PopulationDto pop : popList) {
            dtoList.add(new PopulationChangesDto(pop));
        }
        return GlobalResponseDto.ok("조회 성공", dtoList);
    }




}

