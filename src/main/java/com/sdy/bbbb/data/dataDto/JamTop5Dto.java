package com.sdy.bbbb.data.dataDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JamTop5Dto {

    private Long ww;

    private String areaNm;

    private Long scoreSum;

    private String guNm;


    public JamTop5Dto(JamOfWeek jamOfWeek) {
        this.ww =jamOfWeek.getSavedWeek();
        this.areaNm = jamOfWeek.getAreaNm();
        this.scoreSum = jamOfWeek.getSumOfScore();
        this.guNm = jamOfWeek.getGuNm();
        this.isWeekend = jamOfWeek.getIsWeekend();
    }
}

