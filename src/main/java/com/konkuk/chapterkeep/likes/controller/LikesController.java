package com.konkuk.chapterkeep.likes.controller;

import com.konkuk.chapterkeep.common.response.dto.DataResponseDto;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.likes.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @PostMapping("/book-review/{reviewId}/toggle-like")
    public DataResponseDto<?> bookReviewToggleLike(@PathVariable("reviewId") Long reviewId) {
        likesService.bookReviewToggleLike(reviewId);
        return new DataResponseDto<>(Code.OK, "독서 기록 좋아요 상태 변경 성공");
    }

    @PostMapping("/post/{postId}/toggle-like")
    public DataResponseDto<?> postToggleLike(@PathVariable("postId") Long postId) {
        likesService.postToggleLike(postId);
        return new DataResponseDto<>(Code.OK, "게시글 좋아요 상태 변경 성공");
    }
}
