package com.konkuk.chapterkeep.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id", "post_id"}),
        @UniqueConstraint(columnNames = {"member_id", "comment_id"}),
        @UniqueConstraint(columnNames = {"member_id", "book_review_id"})})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Likes extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_review_id")
    private BookReview bookReview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Builder
    private Likes(BookReview bookReview,
                  Post post, Member member, Comment comment) {
        this.bookReview = bookReview;
        this.post = post;
        this.member = member;
        this.comment = comment;
    }

    public static Likes createLikes(BookReview bookReview,
                                    Post post, Member member, Comment comment) {
        return Likes.builder()
                .bookReview(bookReview)
                .post(post)
                .member(member)
                .comment(comment)
                .build();
    }
}
