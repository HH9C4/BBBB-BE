package com.sdy.bbbb.dto.response.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HourDataDto {

    private Map<String, Long> lastPopByHour;

    private Map<String, Long> todayPopByHour;

}
