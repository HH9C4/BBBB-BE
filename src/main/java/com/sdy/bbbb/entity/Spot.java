package com.sdy.bbbb.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Spot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String spot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Gu gu;

}
