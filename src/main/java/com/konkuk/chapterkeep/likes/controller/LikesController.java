package com.konkuk.chapterkeep.likes.controller;

import com.konkuk.chapterkeep.common.response.dto.DataResponseDto;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.likes.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikesController {

    private final LikesService likesService;

    @PostMapping("/{reviewId}/toggle")
    public DataResponseDto<?> toggleLike(@PathVariable Long reviewId) {
        likesService.toggleLike(reviewId);
        return new DataResponseDto<>(Code.OK, "좋아요 상태 변경 성공");
    }
}
