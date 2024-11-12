package com.konkuk.chapterkeep.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Builder
    private Post(Member member, String title, boolean isAnonymous) {
        this.member = member;
        this.title = title;
        this.isAnonymous = isAnonymous;

        // 양방향 연관관계 설정
        if (member != null) {
            member.getPosts().add(this);
        }
    }

    // 생성 메서드
    public static Post createPost(Member member, String title, boolean isAnonymous) {
        return Post.builder()
                .member(member)
                .title(title)
                .isAnonymous(isAnonymous)
                .build();
    }
}
