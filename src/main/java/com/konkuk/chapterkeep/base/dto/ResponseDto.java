package com.konkuk.chapterkeep.base.dto;

import com.konkuk.chapterkeep.base.constant.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto {

    private final Boolean success; // 성공 여부
    private final int code;  // 응답 코드
    private final String message;   // 응답 메시지

    public static ResponseDto of(Boolean success, Code code) {
        return new ResponseDto(success, code.getCode(), code.getMessage());
    }

    public static ResponseDto of(Boolean success, Code errorCode, Exception e) {
        return new ResponseDto(success, errorCode.getCode(), errorCode.getMessage(e));
    }

    public static ResponseDto of(Boolean success, Code errorCode, String customMessage) {
        return new ResponseDto(success, errorCode.getCode(), errorCode.getMessage(customMessage));
    }

}
