package com.konkuk.chapterkeep.postBoard.controller;

import com.konkuk.chapterkeep.postBoard.dto.PostBoardResDto;
import com.konkuk.chapterkeep.postBoard.service.PostBoardService;
import com.konkuk.chapterkeep.common.response.dto.DataResponseDto;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.postBoard.dto.BookReviewRecommendResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post-board")
public class PostBoardController {

    private final PostBoardService postBoardService;

    @GetMapping("/test")
    public DataResponseDto<List<BookReviewRecommendResDto>> recommendBookByBookReview() {
        List<BookReviewRecommendResDto> response = postBoardService.getMostReviewedBook();
        return new DataResponseDto<>(response, Code.OK, "독서 기록이 많은 순으로 도서 추천 성공");
    }

    @GetMapping
    public DataResponseDto<PostBoardResDto> recommendBookByRentalCount() {
        PostBoardResDto response = postBoardService.getPostBoardData();
        return new DataResponseDto<>(response, Code.OK, "게시판 조회 데이터 응답 성공");
    }
}

