package com.sdy.bbbb.entity;

import com.sdy.bbbb.dto.request.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class HashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Post post;

    @Column
    private String tag;

    public HashTag(Post post, String tag) {
        this.post = post;
        this.tag = tag;
    }
}
