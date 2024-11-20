package com.konkuk.chapterkeep.bookReview.dto;

import com.konkuk.chapterkeep.domain.BookReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookReviewResDto {

    private Long reviewId;
    private BookDto bookInfo;
    private int rating;
    private String quotation;
    private String content;
    private String coverColor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String username;
    private long likesCount;

    public static BookReviewResDto fromEntity(BookReview bookReview, long likesCount) {
        return BookReviewResDto.builder()
                .reviewId(bookReview.getBookReviewId())
                .bookInfo(BookDto.fromEntity(bookReview.getBookInfo()))
                .rating(bookReview.getRating())
                .quotation(bookReview.getQuotation())
                .content(bookReview.getContent())
                .coverColor(bookReview.getCoverColor().name())
                .createdAt(bookReview.getCreatedDate())
                .updatedAt(bookReview.getModifiedDate())
                .username(bookReview.getMember().getName())
                .likesCount(likesCount)
                .build();
    }


}
