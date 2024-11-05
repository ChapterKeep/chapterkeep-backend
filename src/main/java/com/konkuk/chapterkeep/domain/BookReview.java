package com.konkuk.chapterkeep.domain;

import com.konkuk.chapterkeep.domain.enums.CoverColor;
import jakarta.persistence.*;
import lombok.AccessLevel;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private BookInfo bookInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
