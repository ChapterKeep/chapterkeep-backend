package com.konkuk.chapterkeep.postBoard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RentalCountRecommendResDto {

    private String title;
    private String department;
    private int rentalCount;
    private String library_url;
}
