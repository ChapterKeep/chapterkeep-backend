package com.konkuk.chapterkeep.test;

import com.konkuk.chapterkeep.common.response.exception.GeneralException;
import com.konkuk.chapterkeep.common.response.enums.Code;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GeneralExceptionTest {

    @Test
    public void testDefaultGeneralException() {
        GeneralException exception = new GeneralException();

        assertEquals(Code.INTERNAL_ERROR, exception.getErrorCode());
        assertEquals("Internal error", exception.getMessage());

        // 콘솔 출력
        System.out.println("Default GeneralException Message: " + exception.getMessage());
    }

    @Test
    public void testGeneralExceptionWithCustomMessage() {
        String customMessage = "An unexpected error occurred";
        GeneralException exception = new GeneralException(customMessage);

        assertEquals(Code.INTERNAL_ERROR, exception.getErrorCode());
        assertEquals("Internal error - An unexpected error occurred", exception.getMessage());

        // 콘솔 출력
        System.out.println("GeneralException with Custom Message: " + exception.getMessage());
    }

    @Test
    public void testGeneralExceptionWithCause() {
        RuntimeException cause = new RuntimeException("Cause of the error");
        GeneralException exception = new GeneralException(cause);

        assertEquals(Code.INTERNAL_ERROR, exception.getErrorCode());
        assertEquals("Internal error - Cause of the error", exception.getMessage());

        // 콘솔 출력
        System.out.println("GeneralException with Cause: " + exception.getMessage());
    }

    @Test
    public void testGeneralExceptionWithCustomCode() {
        GeneralException exception = new GeneralException(Code.BAD_REQUEST, "Invalid input");

        assertEquals(Code.BAD_REQUEST, exception.getErrorCode());
        assertEquals("Bad request - Invalid input", exception.getMessage());

        // 콘솔 출력
        System.out.println("GeneralException with Custom Code and Message: " + exception.getMessage());
    }
}
