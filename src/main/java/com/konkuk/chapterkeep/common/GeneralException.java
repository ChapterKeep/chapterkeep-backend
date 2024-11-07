package com.konkuk.chapterkeep.common;

import com.konkuk.chapterkeep.common.constant.Code;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

    private final Code errorCode;

    public GeneralException() {
        super(Code.INTERNAL_ERROR.getMessage());
        this.errorCode = Code.INTERNAL_ERROR;
    }

//    public GeneralException(String customMessage) {
//        super(Code.INTERNAL_ERROR.getMessage(customMessage));
//        this.errorCode = Code.INTERNAL_ERROR;
//    }

    public GeneralException(Code errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public GeneralException(Code errorCode, String customMessage) {
        super(errorCode.getMessage(customMessage));
        this.errorCode = errorCode;
    }
}
