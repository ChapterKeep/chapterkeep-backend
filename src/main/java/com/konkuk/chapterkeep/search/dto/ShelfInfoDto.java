package com.konkuk.chapterkeep.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShelfInfoDto {

    private String nickname;
    private String profileUrl;
    private Long bookReviewCount;

}
