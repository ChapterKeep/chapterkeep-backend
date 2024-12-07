package com.konkuk.chapterkeep.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchBookReviewResDto {

    private Long reviewId;
    private String reviewTitle;
    private String coverUrl;
    private String nickname;

}
