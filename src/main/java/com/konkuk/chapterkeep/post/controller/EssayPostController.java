package com.konkuk.chapterkeep.post.controller;

import com.konkuk.chapterkeep.common.response.dto.DataResponseDto;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.domain.Member;
import com.konkuk.chapterkeep.member.service.MemberService;
import com.konkuk.chapterkeep.post.dto.EssayPostListResDto;
import com.konkuk.chapterkeep.post.dto.EssayPostReqDto;
import com.konkuk.chapterkeep.post.dto.EssayPostResDto;
import com.konkuk.chapterkeep.post.service.EssayPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post/essay")
public class EssayPostController {

    private final EssayPostService essayPostService;
    private final MemberService memberService;

    @PostMapping("/create")
    public DataResponseDto<EssayPostResDto> createEssayPost(@RequestBody EssayPostReqDto essayPostReqDto) {
        Member member = memberService.getCurrentMember();
        EssayPostResDto response = essayPostService.createEssayPost(member, essayPostReqDto);
        return new DataResponseDto<>(response, Code.OK, "백일장 게시글 생성 성공");
    }

    @GetMapping("/{postId}")
    public DataResponseDto<EssayPostResDto> getEssayPost(@PathVariable Long postId) {
        EssayPostResDto response = essayPostService.getEssayPost(postId);
        return new DataResponseDto<>(response, Code.OK, "백일장 게시글 조회 성공");
    }

    @GetMapping
    public DataResponseDto<List<EssayPostListResDto>> getAllEssayPost() {
        List<EssayPostListResDto> response = essayPostService.getAllEssayPost();
        return new DataResponseDto<>(response, Code.OK, "백일장 게시글 목록 조회 성공");
    }

    @PutMapping("/{postId}/update")
    public DataResponseDto<EssayPostResDto> updateEssayPost(@PathVariable Long postId, @RequestBody EssayPostReqDto essayPostReqDto) {
        EssayPostResDto response = essayPostService.updateEssayPost(postId, essayPostReqDto);
        return new DataResponseDto<>(response, Code.OK, "백일장 게시글 수정 성공");
    }

    @DeleteMapping("/{postId}/delete")
    public DataResponseDto<EssayPostResDto> deleteEssayPost(@PathVariable Long postId) {
        essayPostService.deletePost(postId);
        return new DataResponseDto<>(null, Code.OK, "백일장 게시글 삭제 성공");
    }

    @GetMapping("/search")
    public DataResponseDto<List<EssayPostListResDto>> searchEssayPost(@RequestParam String keyword) {
        List<EssayPostListResDto> response = essayPostService.searchEssayPost(keyword);
        return new DataResponseDto<>(response, Code.OK, "백일장 게시글 검색 성공");
    }

}
