package com.konkuk.chapterkeep.common.response.dto;

import com.konkuk.chapterkeep.common.response.enums.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto {

    private final String code;  // 응답 코드
    private final String message;   // 응답 메시지


}
