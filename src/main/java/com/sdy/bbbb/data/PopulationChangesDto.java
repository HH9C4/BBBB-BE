package com.sdy.bbbb.data;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PopulationChangesDto {
    private String areaNm;
    private Integer plusMinus;
    private Double increaseRate;

    public PopulationChangesDto(PopulationDto populationDto) {
        this.areaNm = populationDto.getArea_Nm();
        this.plusMinus = populationDto.getPlus_Minus();
        this.increaseRate = populationDto.getIncrease_Rate();
    }
}
