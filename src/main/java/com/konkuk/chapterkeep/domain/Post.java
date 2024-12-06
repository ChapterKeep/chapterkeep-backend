package com.konkuk.chapterkeep.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "title", nullable = false, length = 30)
    private String title;

    @Column(name = "is_anonymous")
    private boolean isAnonymous;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    protected Post(Member member, String title, boolean isAnonymous) {
        this.member = member;
        this.title = title;
        this.isAnonymous = isAnonymous;
    }

    // 연관관계 제거 메서드
    public void removeMember() {
        if (this.member != null) {
            this.member.getPosts().remove(this);
            this.member = null;
        }
    }

    public void update(String title, boolean isAnonymous) {
        this.title = title;
        this.isAnonymous = isAnonymous;
    }
}
