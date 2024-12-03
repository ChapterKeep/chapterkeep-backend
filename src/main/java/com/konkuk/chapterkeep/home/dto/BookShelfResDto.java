package com.konkuk.chapterkeep.home.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookShelfResDto {

    private Long reviewId;
    private String coverUrl;
}
