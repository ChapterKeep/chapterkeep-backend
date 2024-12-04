package com.konkuk.chapterkeep.bookReview.controller;

import com.konkuk.chapterkeep.bookReview.dto.*;
import com.konkuk.chapterkeep.bookReview.service.BookReviewService;
import com.konkuk.chapterkeep.common.response.dto.DataResponseDto;
import com.konkuk.chapterkeep.common.response.enums.Code;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book-review")
public class BookReviewController {

    private final BookReviewService bookReviewService;

    @PostMapping("/create")
    public DataResponseDto<BookReviewCreateResDto> createBookReview(@RequestBody BookReviewReqDto bookReviewReqDto) {
            BookDto bookDto = bookReviewReqDto.getBookInfo();
            BookReviewCreateResDto response = bookReviewService.saveBookReview(bookDto, bookReviewReqDto);
            return new DataResponseDto<>(response, Code.OK, "리뷰가 성공적으로 저장되었습니다.");
    }

    @GetMapping("/{reviewId}")
    public DataResponseDto<BookReviewResDto> getBookReview(@PathVariable("reviewId") Long reviewId) {
        BookReviewResDto response = bookReviewService.getBookReviewById(reviewId);
        return new DataResponseDto<>(response, Code.OK, "독서 기록 조회 성공");
    }

    @PutMapping("/{reviewId}/update")
    public DataResponseDto<BookReviewUpdateResDto> updateBookReview(@PathVariable("reviewId") Long reviewId,
                                                              @RequestBody BookReviewReqDto bookReviewReqDto) {
        BookReviewUpdateResDto response = bookReviewService.updateBookReview(reviewId, bookReviewReqDto);
        return new DataResponseDto<>(response, Code.OK, "독서 기록 수정 성공");
    }

    @DeleteMapping("/{reviewId}/delete")
    public DataResponseDto<String> deleteBookReview(@PathVariable("reviewId") Long reviewId) {
        bookReviewService.deleteBookReview(reviewId);
        return new DataResponseDto<>(Code.OK, "독서 기록 삭제 성공");
    }
}
