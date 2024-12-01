package com.konkuk.chapterkeep.member.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MypagePostListDto {
    private String title;
    private LocalDate createdAt;
    private String nickname;
    private Long postId;
}
