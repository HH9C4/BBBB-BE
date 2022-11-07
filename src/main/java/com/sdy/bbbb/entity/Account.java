package com.sdy.bbbb.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter // get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Entity // DB 테이블 역할을 합니다.
public class Account extends TimeStamped {


    // ID가 자동으로 생성 및 증가합니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    // nullable: null 허용 여부
    // unique: 중복 허용 여부 (false 일때 중복 허용)
    @Column(nullable = false, unique = true)
    private String username = "장윤서";

    @Column(nullable = false)
    private String password = "123";

    @Column(nullable = false, unique = true)
    private String email = "dbstj214@naver.com";

//    @Column(nullable = false)
//    @Enumerated(value = EnumType.STRING)
//    private UserRoleEnum role;

    @Column(unique = true)
    private Long kakaoId = 1L;

    @Column(nullable = true)
    private String gender;

    @Column(nullable = true)
    private String ageRange;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="myPageId")
    private MyPage myPage;

    @OneToMany(mappedBy = "account")
    List<Post> post = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    List<Comment> comment = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    List<Like> likes = new ArrayList<>();

    public Account(String username, String password, String email, Long kakaoId) {
        this.username = username;
        this.password = password;
        this.email = email;
//        this.role = role;
        this.kakaoId = kakaoId;
    }
}
