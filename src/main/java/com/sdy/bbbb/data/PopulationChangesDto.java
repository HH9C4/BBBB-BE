package com.sdy.bbbb.data;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PopulationChangesDto {

    private Integer rowNum;

    private String areaCongestLvl;

    private String areaNm;

    private Long plusMinus;

    private Double increaseRate;


    public PopulationChangesDto(PopulationDto populationDto) {
        this.rowNum = populationDto.getRow_Num();
        this.areaCongestLvl = populationDto.getArea_Congest_Lvl();
        this.areaNm = populationDto.getArea_Nm();
        this.plusMinus = populationDto.getPlus_Minus();
        this.increaseRate = populationDto.getIncrease_Rate();
    }
}
