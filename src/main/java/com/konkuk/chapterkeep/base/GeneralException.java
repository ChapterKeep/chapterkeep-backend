package com.konkuk.chapterkeep.base;

import com.konkuk.chapterkeep.base.constant.Code;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

    private final Code errorCode;

    public GeneralException() {
        super(Code.INTERNAL_ERROR.getMessage());
        this.errorCode = Code.INTERNAL_ERROR;
    }

    public GeneralException(String customMessage) {
        super(Code.INTERNAL_ERROR.getMessage(customMessage));
        this.errorCode = Code.INTERNAL_ERROR;
    }

    public GeneralException(String customMessage, Throwable cause) {
        super(Code.INTERNAL_ERROR.getMessage(customMessage), cause);
        this.errorCode = Code.INTERNAL_ERROR;
    }

    public GeneralException(Throwable cause) {
        super(Code.INTERNAL_ERROR.getMessage(cause));
        this.errorCode = Code.INTERNAL_ERROR;
    }

    public GeneralException(Code errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public GeneralException(Code errorCode, String customMessage) {
        super(errorCode.getMessage(customMessage));
        this.errorCode = errorCode;
    }

    public GeneralException(Code errorCode, String customMessage, Throwable cause) {
        super(errorCode.getMessage(customMessage), cause);
        this.errorCode = errorCode;
    }

    public GeneralException(Code errorCode, Throwable cause) {
        super(errorCode.getMessage(cause), cause);
        this.errorCode = errorCode;
    }
}
