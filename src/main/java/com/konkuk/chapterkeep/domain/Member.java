package com.konkuk.chapterkeep.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "name", nullable = false, length = 10)
    private String name;

    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @Column(name = "nickname", nullable = false, length = 10)
    private String nickname;

    @Column(name = "introduction", length = 80)
    private String introduction;

    @Column(name = "department", nullable = false, length = 25)
    private String department;

    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "role", nullable = false, length = 10)
    private boolean role = true;

    @Column(name = "visibility", nullable = false)
    private boolean visibility = true;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookReview> bookReviews = new ArrayList<>();


    @Builder
    public Member(String name, String password, String nickname, String introduction, String department, String profileUrl, Boolean role, Boolean visibility) {
        this.name = name;
        this.password = password;
        this.nickname = nickname;
        this.introduction = introduction;
        this.department = department;
        this.profileUrl = profileUrl;
        this.role = role;
        this.visibility = visibility;
    }
}