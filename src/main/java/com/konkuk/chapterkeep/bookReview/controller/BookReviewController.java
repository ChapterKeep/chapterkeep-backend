package com.konkuk.chapterkeep.bookReview.controller;

import com.konkuk.chapterkeep.bookReview.dto.BookDto;
import com.konkuk.chapterkeep.bookReview.dto.BookReviewReqDto;
import com.konkuk.chapterkeep.bookReview.dto.BookReviewResDto;
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
    public DataResponseDto<BookReviewResDto> createBookReview(@RequestBody BookReviewReqDto bookReviewReqDto) {
            BookDto bookDto = bookReviewReqDto.getBookInfo();
            BookReviewResDto response = bookReviewService.saveBookReview(bookDto, bookReviewReqDto);
            return new DataResponseDto<>(response, Code.OK, "리뷰가 성공적으로 저장되었습니다.");
    }

//    @GetMapping("/reviews")
//    public ResponseEntity<List<BookReview>> getAllBookReview() {
//        List<BookReview> bookReviews = bookReviewService.getBookReviews();
//        return ResponseEntity.ok(bookReviews);
//    }

    @GetMapping("/{reviewId}")
    public DataResponseDto<BookReviewResDto> getBookReview(@PathVariable("reviewId") Long reviewId) {
        BookReviewResDto response = bookReviewService.getBookReviewById(reviewId);
        return new DataResponseDto<>(response, Code.OK, "리뷰 조회 성공");
    }

}
