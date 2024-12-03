package com.konkuk.chapterkeep.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EssayPostReqDto {

    private String title;
    private boolean anonymous;
    private String content;

}
