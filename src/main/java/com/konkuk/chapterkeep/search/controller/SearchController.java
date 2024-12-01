package com.konkuk.chapterkeep.search.controller;

import com.konkuk.chapterkeep.common.response.dto.DataResponseDto;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.search.dto.SearchBookReviewResDto;
import com.konkuk.chapterkeep.search.dto.SearchBookShelfResDto;
import com.konkuk.chapterkeep.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/book-review")
    public DataResponseDto<SearchBookReviewResDto> searchBookReviews(@RequestParam("title") String title) {
        SearchBookReviewResDto response = searchService.getReviewsByTitle(title);
        return new DataResponseDto<>(response, Code.OK, "검색 성공");
    }

    @GetMapping("/book-shelf")
    public DataResponseDto<SearchBookShelfResDto> searchBookSelf(@RequestParam("nickname") String nickname) {
        SearchBookShelfResDto response = searchService.getBookShelfByNickName(nickname);
        return new DataResponseDto<>(response, Code.OK, "검색 성공");
    }
}
