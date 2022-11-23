package com.sdy.bbbb.data.dataDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SpotCalculatedDto {

    private String guNm;
    private String areaNm;
    private String DayOfWeek;
    private String yearMonthDate;
    private String thatHour;
    private String populationByHour;

    public SpotCalculatedDto(SpotCalculated spotCalculated) {
        this.guNm = spotCalculated.getGu_Nm();
        this.areaNm = spotCalculated.getArea_Nm();
        this.DayOfWeek = spotCalculated.getDay_Of_Week();
        this.yearMonthDate = spotCalculated.getYear_Month_Date();
        this.thatHour = spotCalculated.getThat_Hour();
        this.populationByHour = spotCalculated.getPopulation_By_Hour();
    }
}
