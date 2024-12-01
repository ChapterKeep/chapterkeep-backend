package com.konkuk.chapterkeep.member.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MypageResDto {
    private String nickname;
    private Long postCount;
    private List<MypagePostListDto> myPosts;
    private List<MypagePostListDto> commentedPosts;
    private List<MypagePostListDto> likedPosts;
}
