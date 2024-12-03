package com.konkuk.chapterkeep.postBoard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookReviewRecommendResDto {

    private String title;
    private String writer;
    private String cover_url;

}
