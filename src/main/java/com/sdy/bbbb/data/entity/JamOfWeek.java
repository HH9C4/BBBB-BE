package com.sdy.bbbb.data.entity;

import com.sdy.bbbb.data.dataDto.JamDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class JamOfWeek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long sumOfScore;

    @Column
    private String areaNm;

    @Column
    private String guNm;

    @Column
    private Long savedWeek;

    @Column
    private Boolean isWeekend;


    public JamOfWeek(JamDto jamDto, Boolean isWeekend) {
        this.sumOfScore = jamDto.getScore_Sum();
        this.areaNm = jamDto.getArea_Nm();
        this.guNm = jamDto.getGu_Nm();
        this.savedWeek = jamDto.getWw();
        this.isWeekend = isWeekend;
    }
}
