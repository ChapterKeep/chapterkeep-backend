package com.konkuk.chapterkeep.bookReview.dto;

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
    private String quote;
    private String description;
    private String coverColor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
