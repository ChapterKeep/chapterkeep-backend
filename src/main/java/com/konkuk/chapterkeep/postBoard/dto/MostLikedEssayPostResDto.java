package com.konkuk.chapterkeep.postBoard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MostLikedEssayPostResDto {

    private String nickname;
    private String profileUrl;
    private boolean anonymous;
    private Long postId;
    private String postTitle;
    private long likesCount;

}
