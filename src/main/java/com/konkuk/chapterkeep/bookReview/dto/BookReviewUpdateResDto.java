package com.konkuk.chapterkeep.bookReview.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BookReviewUpdateResDto {

    private Long reviewId;
    private LocalDateTime modifiedAt;

}