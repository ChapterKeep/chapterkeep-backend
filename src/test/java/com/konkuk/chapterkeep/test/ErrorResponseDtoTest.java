package com.konkuk.chapterkeep.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konkuk.chapterkeep.base.constant.Code;
import com.konkuk.chapterkeep.base.dto.ErrorResponseDto;
import com.konkuk.chapterkeep.base.dto.ResponseDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ErrorResponseDtoTest {

    @Test
    public void testErrorResponseCreation() throws Exception {
        ErrorResponseDto response = ErrorResponseDto.from(Code.BAD_REQUEST);

        printResponse(response); // JSON 출력

        assertFalse(response.getSuccess());
        assertEquals(10400, response.getCode());
        assertEquals("Bad request", response.getMessage());
    }

    @Test
    public void testErrorResponseWithException() throws Exception {
        Exception e = new RuntimeException("Database error");
        ErrorResponseDto response = ErrorResponseDto.of(Code.INTERNAL_ERROR, e);

        printResponse(response); // JSON 출력

        assertFalse(response.getSuccess());
        assertEquals(10500, response.getCode());
        assertEquals("Internal error - Database error", response.getMessage());
    }

    @Test
    public void testErrorResponseWithCustomMessage() throws Exception {
        String customMessage = "Custom error message";
        ErrorResponseDto response = ErrorResponseDto.of(Code.NOT_FOUND, customMessage);

        printResponse(response); // JSON 출력

        assertFalse(response.getSuccess());
        assertEquals(10404, response.getCode());
        assertEquals("Requested resource is not found - Custom error message", response.getMessage());
    }

    private void printResponse(ResponseDto response) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
        System.out.println(jsonString); // 콘솔에 JSON 출력
    }
}