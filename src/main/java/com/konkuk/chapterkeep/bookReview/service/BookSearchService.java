package com.konkuk.chapterkeep.bookReview.service;

import com.konkuk.chapterkeep.bookReview.dto.BookDto;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.common.response.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class BookSearchService {

    @Value("${naver.api.client-id}")
    private String clientId;

    @Value("${naver.api.client-secret}")
    private String clientSecret;

    public BookDto searchBooks(String keyword) {
        // 요청 파라미터 유효성 검증
        validateKeyword(keyword);

        try {
            // API 요청 URL 생성
            String apiURL = "https://openapi.naver.com/v1/search/book.json?query=" + keyword;

            // RestTemplate 초기화 및 헤더 설정
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", clientId);
            headers.set("X-Naver-Client-Secret", clientSecret);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // API 호출
            ResponseEntity<String> response = restTemplate.exchange(apiURL, HttpMethod.GET, entity, String.class);

            // 상태 코드 확인
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new GeneralException(Code.EXTERNAL_API_ERROR, "도서 검색 API 호출 실패");
            }

            // JSON 응답 데이터 파싱
            return parseBookResponse(response.getBody());

        } catch (GeneralException e) {
            throw e;
        } catch (Exception e){
            throw new GeneralException(Code.INTERNAL_ERROR, "도서 검색 중 오류 발생 : " + e.getMessage());
        }
    }

    private void validateKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new GeneralException(Code.BAD_REQUEST, "유효하지 않은 ISBN 키워드");
        }
    }

    private BookDto parseBookResponse(String responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            if (jsonArray.length() > 0) {
                JSONObject obj = jsonArray.getJSONObject(0);
                String title = obj.getString("title");
                String writer = obj.getString("author");
                String coverUrl = obj.getString("image");

                // BookDto 생성 후 반환
                return BookDto.builder()
                        .title(title)
                        .writer(writer)
                        .coverUrl(coverUrl)
                        .build();
            } else {
                throw new GeneralException(Code.NOT_FOUND, "검색 결과가 존재하지 않음");
            }
        } catch (JSONException e) {
                throw new GeneralException(Code.INTERNAL_ERROR, "JSON 파싱 오류: " + e.getMessage());
        } catch (GeneralException e) {
            throw e;
        } catch (Exception e){
            throw new GeneralException(Code.INTERNAL_ERROR, "도서 데이터 처리 중 오류 발생: " + e.getMessage());
        }
    }
}
