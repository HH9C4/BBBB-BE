package com.sdy.bbbb.data.dataDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DataResponseDto {

    List<JamTop5Dto> jamTopList;

    List<PopulationChangesDto> popChangeList;
}
