package com.konkuk.chapterkeep.bookReview.controller;

import com.konkuk.chapterkeep.bookReview.dto.BookDto;
import com.konkuk.chapterkeep.bookReview.service.BookSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookSearchController {

    private final BookSearchService bookSearchService;

    @GetMapping("/book-search")
    public ResponseEntity<List<BookDto>> searchBooks(@RequestParam("keyword") String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            // 서비스에서 도서 검색 결과 가져오기
            List<BookDto> bookDtoList = bookSearchService.searchBooks(keyword);
            return ResponseEntity.ok(bookDtoList); // JSON 형식으로 반환
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}