package com.konkuk.chapterkeep.bookReview.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BookReviewCreateResDto {

    private Long reviewId;
    private LocalDateTime createdAt;

}
