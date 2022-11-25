package com.sdy.bbbb.data.dataDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DataTestDto {
    private Map<String, Long> lastPopByHour;
    private Map<String, Long> todayPopByHour;


}
