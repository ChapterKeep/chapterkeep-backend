package com.konkuk.chapterkeep.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.common.response.dto.ResponseDto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ResponseDtoTest {

    @Test
    public void testSuccessfulResponse() throws Exception {
        ResponseDto response = ResponseDto.of(true, Code.OK);

        printResponse(response); // JSON 출력

        assertTrue(response.getSuccess());
        assertEquals(10200, response.getCode());
        assertEquals("OK", response.getMessage());
    }

    @Test
    public void testErrorResponseWithCustomMessage() throws Exception {
        String customMessage = "An error occurred";
        ResponseDto response = ResponseDto.of(false, Code.INTERNAL_ERROR, customMessage);

        printResponse(response); // JSON 출력

        assertFalse(response.getSuccess());
        assertEquals(10500, response.getCode());
        assertEquals("Internal error - " + customMessage, response.getMessage());
    }

    @Test
    public void testErrorResponseWithNullMessage() throws Exception {
        ResponseDto response = ResponseDto.of(false, Code.INTERNAL_ERROR, (String) null);

        printResponse(response); // JSON 출력

        assertFalse(response.getSuccess());
        assertEquals(10500, response.getCode());
        assertEquals("Internal error", response.getMessage()); // 기본 메시지 확인
    }

    private void printResponse(ResponseDto response) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
        System.out.println(jsonString); // 콘솔에 JSON 출력
    }
}
