package com.konkuk.chapterkeep.base.dto;

import com.konkuk.chapterkeep.base.constant.Code;

public class ErrorResponseDto extends ResponseDto{

    private ErrorResponseDto(Code errorCode) {
        super(false, errorCode.getCode(), errorCode.getMessage());
    }

    private ErrorResponseDto(Code errorCode, Exception e) {
        super(false, errorCode.getCode(), errorCode.getMessage(e));
    }

    private ErrorResponseDto(Code errorCode, String customMessage) {
        super(false, errorCode.getCode(), errorCode.getMessage(customMessage));
    }

    public static ErrorResponseDto from(Code errorCode) {
        return new ErrorResponseDto(errorCode);
    }

    public static ErrorResponseDto of(Code errorCode, Exception e) {
        return new ErrorResponseDto(errorCode, e);
    }

    public static ErrorResponseDto of(Code errorCode, String customMessage) {
        return new ErrorResponseDto(errorCode, customMessage);
    }
}
