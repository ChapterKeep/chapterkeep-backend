package com.konkuk.chapterkeep.common.response.dto;

import com.konkuk.chapterkeep.common.response.enums.Code;

public class ErrorResponseDto extends ResponseDto {

    public ErrorResponseDto(Code code) {
        super(code.getCode(), code.getMessage());
    }

    public ErrorResponseDto(Code code, String message) {
        super(code.getCode(), message);
    }

}
