package com.sdy.bbbb.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter // get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Entity
@AllArgsConstructor
@Builder// DB 테이블 역할을 합니다.
public class Account extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nullable: null 허용 여부
    // unique: 중복 허용 여부 (false 일때 중복 허용)
    @Column(nullable = false)
    private String accountName;

    @Column(nullable = false)
    private String password ;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true)
    private Long kakaoId;

    @Column(unique = true)
    private String naverId;

    @Column(nullable = true)
    private String profileImage;

    @Column(nullable = true)
    private String gender;

    @Column(nullable = true)
    private String ageRange;

    @Column(nullable = false)
    private int reportedCount;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="myPageId")
    private MyPage myPage;

    @OneToMany(mappedBy = "account")
    List<Post> post = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    List<Comment> comment = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    List<Bookmark> bookmarks = new ArrayList<>();


    public Account(String nickname, String password, String email, String profileImage, Long kakaoId, String gender, String ageRange) {
        this.accountName = nickname;
        this.password = password;
        this.profileImage = profileImage;
        this.email = email;
        this.kakaoId = kakaoId;
        this.gender = gender;
        this.ageRange = ageRange;
        this.reportedCount = 0;
    }

    public Account(String nickname, String password, String email, String profileImage, String naverId, String gender, String ageRange) {
        this.accountName = nickname;
        this.password = password;
        this.profileImage = profileImage;
        this.email = email;
        this.naverId = naverId;
        this.gender = gender;
        this.ageRange = ageRange;
        this.reportedCount = 0;
    }

}
