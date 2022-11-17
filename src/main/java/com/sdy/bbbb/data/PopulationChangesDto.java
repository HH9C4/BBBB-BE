package com.sdy.bbbb.data;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PopulationChangesDto {

    private String areaNm;

    private Long plusMinus;

    private Double increaseRate;

    private String areaCongestLvl;

    private Integer rowNum;

    public PopulationChangesDto(PopulationDto populationDto) {
        this.areaNm = populationDto.getArea_Nm();
        this.plusMinus = populationDto.getPlus_Minus();
        this.increaseRate = populationDto.getIncrease_Rate();
        this.areaCongestLvl = populationDto.getArea_Congest_Lvl();
        this.rowNum = populationDto.getRow_Num();
    }
}
