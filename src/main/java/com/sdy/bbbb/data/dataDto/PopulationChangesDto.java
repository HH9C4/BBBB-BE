package com.sdy.bbbb.data.dataDto;

import com.sdy.bbbb.data.dataDto.PopulationDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PopulationChangesDto {

    private Integer rowNum;

    private String areaNm;

    private String areaCongestLvl;

    private Long plusMinus;

    private Double increaseRate;

    private String guNm;


    public PopulationChangesDto(PopulationDto populationDto) {
        this.rowNum = populationDto.getRow_Num();
        this.areaNm = populationDto.getArea_Nm();
        this.areaCongestLvl = populationDto.getArea_Congest_Lvl();
        this.plusMinus = populationDto.getPlus_Minus();
        this.increaseRate = populationDto.getIncrease_Rate();
        this.guNm = populationDto.getGu_Nm();
    }
}
