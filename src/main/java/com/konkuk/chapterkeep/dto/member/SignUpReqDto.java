package com.konkuk.chapterkeep.dto.member;

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
