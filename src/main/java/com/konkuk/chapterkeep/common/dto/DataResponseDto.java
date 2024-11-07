package com.konkuk.chapterkeep.common.dto;

import com.konkuk.chapterkeep.common.constant.Code;
import lombok.Getter;

@Getter
public class DataResponseDto<T> extends ResponseDto {

    private final T data;

    private DataResponseDto(T data) {
        super(true, Code.OK.getCode(), Code.OK.getMessage());
        this.data = data;
    }

    private DataResponseDto(T data, String customMessage) {
        super(true, Code.OK.getCode(), customMessage);
        this.data = data;
    }

    public static <T> DataResponseDto<T> of(T data) {
        return new DataResponseDto<>(data);
    }

    public static <T> DataResponseDto<T> of(T data, String customMessage) {
        return new DataResponseDto<>(data, customMessage);
    }

    public static <T> DataResponseDto<T> empty() {
        return new DataResponseDto<>(null);
    }
}