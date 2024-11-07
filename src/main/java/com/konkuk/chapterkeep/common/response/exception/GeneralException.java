package com.konkuk.chapterkeep.common.response.exception;

import com.konkuk.chapterkeep.common.response.enums.Code;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

    private final Code code;


    public GeneralException(Code code) {
        super(code.getMessage());
        this.code = code;
    }

    public GeneralException(Code code, String customMessage) {
        super(code.getMessage(customMessage));
        this.code = code;
    }

}
