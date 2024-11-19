package com.konkuk.chapterkeep.domain;

import com.konkuk.chapterkeep.domain.enums.CoverColor;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book_review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookReview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_review_id")
    private Long bookReviewId;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Column(name = "quotation", length = 50)
    private String quotation;

    @Enumerated(EnumType.STRING)
    private CoverColor coverColor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private BookInfo bookInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private BookReview(Member member, BookInfo bookInfo, int rating, String content, String quotation,
                       CoverColor coverColor) {
        this.member = member;
        this.bookInfo = bookInfo;
        this.rating = rating;
        this.content = content;
        this.quotation = quotation;
        this.coverColor = coverColor;

        // 연관관계 설정
        if (member != null) {
            member.getBookReviews().add(this);
        }
    }

    // 생성 메서드
    public static BookReview createBookReview(Member member, int rating, String content, String quotation,
                                              CoverColor coverColor, BookInfo bookInfo) {
        return BookReview.builder()
                .member(member)
                .rating(rating)
                .content(content)
                .quotation(quotation)
                .coverColor(coverColor)
                .bookInfo(bookInfo)
                .build();
    }

    // 연관 관계 해제 메서드
    public void removeMember() {
        if (member != null) {
            member.getBookReviews().remove(this);
            this.member = null;
        }
    }
}
