package com.sdy.bbbb.data.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class CongestionOfWeek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String sumOfScore;

    @Column
    private String areaNm;

    @Column
    private String savedWeek;

}
