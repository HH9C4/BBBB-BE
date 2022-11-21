package com.sdy.bbbb.data.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class JamOfWeek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String sumOfScore;

    @Column
    private String areaNm;

    @Column
    private String guNm;

    @Column
    private String savedWeek;

    @Column
    private Boolean isWeekend;


    public JamOfWeek(JamDto jamDto, Boolean isWeekend) {
        this.sumOfScore = jamDto.getScore_Num();
        this.areaNm = jamDto.getArea_Nm();
        this.guNm = jamDto.getGu_Nm();
        this.savedWeek = jamDto.getWw();
        this.isWeekend = isWeekend;
    }
}
