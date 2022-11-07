package com.sdy.bbbb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sdy.bbbb.dto.request.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter @Getter
@NoArgsConstructor
public class Post extends TimeStamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

//    @JoinColumn(nullable = false)
//    @ManyToOne
//    private Gu gu;

    @Column(nullable = false)
    private String gu;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private int commentCount;

    @Column(nullable = false)
    private int likeCount;

    @Column(nullable = false)
    private int views;

//    @OneToMany(mappedBy = "post")
//    private List<Comment> commentList;

    @OneToMany(mappedBy = "post")
    @JsonIgnore
    private List<Like> likeList;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Image> imageList = new ArrayList();


    public Post(PostRequestDto postRequestDto, Account account) {
        this.content = postRequestDto.getContent();
        this.category = postRequestDto.getCategory();
        this.account = account;
        this.gu = postRequestDto.getGu();
    }

    public void update(PostRequestDto postRequestDto) {
        this.content = postRequestDto.getContent();
        this.category = postRequestDto.getCategory();
        this.gu = postRequestDto.getGu();
    }
}
