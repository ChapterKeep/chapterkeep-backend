package com.konkuk.chapterkeep.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "is_anonymous", nullable = false)
    private boolean isAnonymous;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    private Comment(Member member, Post post, String content, boolean isAnonymous) {
        this.member = member;
        this.post = post;
        this.content = content;
        this.isAnonymous = isAnonymous;

        // 양방향 연관 관계 설정
        if (member != null) {
            member.getComments().add(this);
        }
        if (post != null) {
            post.getComments().add(this);
        }
    }

    // 생성 메서드
    public static Comment createComment(Member member, Post post, String content, boolean isAnonymous) {
        return Comment.builder()
                .member(member)
                .post(post)
                .content(content)
                .isAnonymous(isAnonymous)
                .build();
    }

    // 연관 관계 해제 메서드 (member)
    public void removeMember() {
        if (member != null) {
            member.getComments().remove(this);
            this.member = null;
        }
    }

    // 연관 관계 해제 메서드 (post)
    public void removePost() {
        if (post != null) {
            post.getComments().remove(this);
            this.post = null;
        }
    }
}
