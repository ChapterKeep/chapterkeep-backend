package com.konkuk.chapterkeep.common.response.dto;

import com.konkuk.chapterkeep.common.response.enums.Code;
import lombok.Getter;

@Getter
public class DataResponseDto<T> extends ResponseDto {

    private final T data;

    public DataResponseDto(T data, Code code) {
        super(code.getCode(), code.getMessage());
        this.data = data;
    }

    public DataResponseDto(T data, Code code, String customMessage) {
        super(code.getCode(), code.getMessage(customMessage));
        this.data = data;
    }

}