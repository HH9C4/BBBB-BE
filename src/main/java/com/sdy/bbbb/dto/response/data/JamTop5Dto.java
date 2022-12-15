package com.sdy.bbbb.dto.response.data;

import com.sdy.bbbb.entity.data.JamOfWeek;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JamTop5Dto {

    private Long ranking;

    private String areaNm;

    private Long scoreSum;

    private String guNm;

    private Boolean isWeekend;


    public JamTop5Dto(JamOfWeek jamOfWeek) {
        this.ranking = jamOfWeek.getRanking();
        this.areaNm = jamOfWeek.getAreaNm();
        this.scoreSum = jamOfWeek.getSumOfScore();
        this.guNm = jamOfWeek.getGuNm();
        this.isWeekend = jamOfWeek.getIsWeekend();
    }

    public JamTop5Dto(Long ranking, JamDto jamDto, boolean isWeekend) {
        this.ranking = ranking;
        this.areaNm = jamDto.getArea_Nm();
        this.scoreSum = jamDto.getScore_Sum();
        this.guNm = jamDto.getGu_Nm();
        this.isWeekend = isWeekend;
    }

}

