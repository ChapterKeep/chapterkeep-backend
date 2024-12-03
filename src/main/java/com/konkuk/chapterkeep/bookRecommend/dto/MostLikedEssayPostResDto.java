package com.konkuk.chapterkeep.bookRecommend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MostLikedEssayPostResDto {

    private String nickname;
    private String profileUrl;
    private boolean anonymous;
    private String title;
    private long likesCount;

}
