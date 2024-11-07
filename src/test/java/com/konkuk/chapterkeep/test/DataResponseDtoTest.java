package com.konkuk.chapterkeep.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konkuk.chapterkeep.base.dto.DataResponseDto;
import com.konkuk.chapterkeep.base.dto.ResponseDto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DataResponseDtoTest {

    @Test
    public void testOfWithData() throws Exception {
        String data = "Hello, World!";
        DataResponseDto<String> response = DataResponseDto.of(data);

        printResponse(response); // JSON 출력

        assertNotNull(response);
        assertEquals(data, response.getData());
        assertEquals("OK", response.getMessage());
    }

    @Test
    public void testOfWithDataAndCustomMessage() throws Exception {
        String data = "Hello, World!";
        String customMessage = "Custom Success Message";
        DataResponseDto<String> response = DataResponseDto.of(data, customMessage);

        printResponse(response); // JSON 출력

        assertNotNull(response);
        assertEquals(data, response.getData());
        assertEquals(customMessage, response.getMessage());
    }

    @Test
    public void testEmpty() throws Exception {
        DataResponseDto<String> response = DataResponseDto.empty();

        printResponse(response); // JSON 출력

        assertNotNull(response);
        assertNull(response.getData());
        assertEquals("OK", response.getMessage());
    }

    private void printResponse(ResponseDto response) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
        System.out.println(jsonString); // 콘솔에 JSON 출력
    }

}
