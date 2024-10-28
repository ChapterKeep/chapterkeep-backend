package com.konkuk.chapterkeep.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Member")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "role", nullable = false)
    private boolean role;

    @Column(name = "visibility", nullable = false)
    private boolean visibility = true;

}
