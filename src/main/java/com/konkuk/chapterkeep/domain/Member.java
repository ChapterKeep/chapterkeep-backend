package com.konkuk.chapterkeep.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.konkuk.chapterkeep.domain.enums.Role;
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

    @Column(name = "profile_url")
    private String profileUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "visibility", nullable = false)
    private Boolean visibility = true;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<BookReview> bookReviews = new ArrayList<>();


    @Builder
    public Member(String name, String password, String nickname, String introduction,
                  String profileUrl, Role role, Boolean visibility) {
        this.name = name;
        this.password = password;
        this.nickname = nickname;
        this.introduction = introduction;
        this.profileUrl = profileUrl;
        this.role = role;
        this.visibility = visibility;
    }

    public static Member createMember(String name, String password, String nickname, String introduction,
                                      String profileUrl, Role role, Boolean visibility) {
        return Member.builder()
                .name(name)
                .password(password)
                .nickname(nickname)
                .introduction(introduction)
                .profileUrl(profileUrl)
                .role(role)
                .visibility(visibility)
                .build();
    }
}

