package com.konkuk.chapterkeep.member.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignUpReqDto {
    private String id;
    private String password;
    private String nickname;
    private String introduction;
}
