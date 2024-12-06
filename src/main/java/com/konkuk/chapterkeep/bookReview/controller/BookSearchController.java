package com.konkuk.chapterkeep.bookReview.controller;

import com.konkuk.chapterkeep.bookReview.dto.BookDto;
import com.konkuk.chapterkeep.bookReview.service.BookSearchService;
import com.konkuk.chapterkeep.common.response.dto.DataResponseDto;
import com.konkuk.chapterkeep.common.response.enums.Code;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookSearchController {

    private final BookSearchService bookSearchService;

    @GetMapping("/book-search")
    public DataResponseDto<BookDto> searchBook(@RequestParam("isbn") String isbn) {
        BookDto response = bookSearchService.searchBook(isbn);
        return new DataResponseDto<>(response, Code.OK, "도서 검색 성공");
    }
}