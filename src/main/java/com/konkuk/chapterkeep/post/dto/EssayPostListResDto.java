package com.konkuk.chapterkeep.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EssayPostListResDto {

    private String title;
    private String nickname;
    private long likesCount;
}
