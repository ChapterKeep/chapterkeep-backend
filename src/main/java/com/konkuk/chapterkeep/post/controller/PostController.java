package com.konkuk.chapterkeep.post.controller;

import com.konkuk.chapterkeep.common.response.dto.DataResponseDto;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.post.dto.EssayPostReqDto;
import com.konkuk.chapterkeep.post.dto.EssayPostResDto;
import com.konkuk.chapterkeep.post.service.EssayPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final EssayPostService essayPostService;

    @PostMapping("/essay/create")
    public DataResponseDto<EssayPostResDto> createEssayPost(@RequestBody EssayPostReqDto essayPostReqDto) {
        EssayPostResDto response = essayPostService.createPost(essayPostReqDto);
        return new DataResponseDto<>(response, Code.OK, "게시글 생성 성공");
    }

    @PutMapping("/essay/{postId}/update")
    public DataResponseDto<EssayPostResDto> updateEssayPost(@PathVariable Long postId, @RequestBody EssayPostReqDto essayPostReqDto) {
        EssayPostResDto response = essayPostService.updateEssayPost(postId, essayPostReqDto);
        return new DataResponseDto<>(response, Code.OK, "게시글 수정 성공");
    }

    @DeleteMapping("/essay/{postId}/delete")
    public DataResponseDto<EssayPostResDto> deleteEssayPost(@PathVariable Long postId) {
        essayPostService.deletePost(postId);
        return new DataResponseDto<>(Code.OK, "게시글 삭제 성공");
    }
}
