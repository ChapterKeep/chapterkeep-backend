package com.konkuk.chapterkeep.home.controller;

import com.konkuk.chapterkeep.bookReview.service.BookReviewService;
import com.konkuk.chapterkeep.common.response.dto.DataResponseDto;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.domain.BookReview;
import com.konkuk.chapterkeep.home.dto.HomeResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    private final BookReviewService bookReviewService;

    @GetMapping
    public DataResponseDto<HomeResDto> getBookReviews() {
        HomeResDto response = bookReviewService.getBookReviews();
        return new DataResponseDto<>(response, Code.OK, "홈 화면 데이터를 성공적으로 가져왔습니다.");
    }
}
