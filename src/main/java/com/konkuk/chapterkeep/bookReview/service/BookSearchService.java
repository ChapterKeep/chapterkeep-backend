package com.konkuk.chapterkeep.bookReview.service;

import com.konkuk.chapterkeep.bookReview.dto.BookDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookSearchService {

    @Value("${naver.api.client-id}")
    private String clientId;

    @Value("${naver.api.client-secret}")
    private String clientSecret;

    public List<BookDto> searchBooks(String keyword) {
        try {
            // 검색 키워드 인코딩
            String apiURL = "https://openapi.naver.com/v1/search/book.json?query=" + keyword;
            System.out.println(keyword);
            System.out.println("API URL: " + apiURL);

            // RestTemplate 초기화 및 헤더 설정
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", clientId);
            headers.set("X-Naver-Client-Secret", clientSecret);
            System.out.println(clientId);
            System.out.println(clientSecret);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // API 요청
            ResponseEntity<String> response = restTemplate.exchange(apiURL, HttpMethod.GET, entity, String.class);
            System.out.println("Response Status: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());

            // 상태 코드 확인
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to fetch books: " + response.getStatusCode());
            }

            // JSON 응답 데이터 파싱
            return parseBookResponse(response.getBody());

        } catch (Exception e) {
            e.printStackTrace(); // 디버깅 로그 추가
            throw new RuntimeException("Error while searching books: " + e.getMessage(), e);
        }
    }

    private List<BookDto> parseBookResponse(String responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            List<BookDto> bookDtoList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String title = obj.getString("title");
                String writer = obj.getString("author");
                String coverUrl = obj.getString("image");

                bookDtoList.add(BookDto.builder()
                        .title(title)
                        .writer(writer)
                        .coverUrl(coverUrl)
                        .build());
            }
            return bookDtoList;

        } catch (Exception e) {
            throw new RuntimeException("Error parsing response JSON: " + e.getMessage(), e);
        }
    }
}