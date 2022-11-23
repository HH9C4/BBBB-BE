package com.sdy.bbbb.data.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class AllPopulationByHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String dayOfWeek;

    @Column
    private String yearMonthDate;

    @Column
    private String areaNm;

    @Column
    private String thatHour;

    @Column
    private String populationByHour;

}
