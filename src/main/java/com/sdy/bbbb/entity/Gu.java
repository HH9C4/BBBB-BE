package com.sdy.bbbb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Gu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String guName;

    @OneToMany(mappedBy = "gu", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Spot> spot;

}
