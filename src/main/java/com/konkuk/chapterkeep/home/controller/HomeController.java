package com.konkuk.chapterkeep.home.controller;

import com.konkuk.chapterkeep.bookReview.service.BookReviewService;
import com.konkuk.chapterkeep.common.response.dto.DataResponseDto;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.domain.Member;
import com.konkuk.chapterkeep.home.dto.HomeResDto;
import com.konkuk.chapterkeep.member.dto.CustomMemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public DataResponseDto<HomeResDto> getHome(@AuthenticationPrincipal CustomMemberDetails customMemberDetails) {
        Member member = customMemberDetails.getMember();
        List<String> coverImageUrls = bookReviewService.getBookCoverImageUrlsByUser(member.getMemberId());
        HomeResDto homeResDto = new HomeResDto(coverImageUrls);
        return new DataResponseDto<>(homeResDto, Code.OK, "홈 화면 데이터를 성공적으로 가져왔습니다.");
    }
}
