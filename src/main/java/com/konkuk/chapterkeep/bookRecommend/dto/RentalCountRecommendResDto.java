package com.konkuk.chapterkeep.bookRecommend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RentalCountRecommendResDto {

    private String title;
    private String library_url;
}
