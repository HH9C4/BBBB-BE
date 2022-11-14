package com.sdy.bbbb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private boolean bookmarked;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Account account;

    @ManyToOne
    @JoinColumn
    private Gu gu;

    public Bookmark(Gu gu, Account account) {
        this.gu = gu;
        this.account = account;
    }

    public void updateBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }
}
