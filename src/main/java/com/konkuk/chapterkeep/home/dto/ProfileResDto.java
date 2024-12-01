package com.konkuk.chapterkeep.home.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResDto {
        private String nickname;
        private String introduction;
        private String profileUrl;
        private Boolean visibility;
        private Long postCount;
}
