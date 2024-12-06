package com.konkuk.chapterkeep.home.controller;

import com.konkuk.chapterkeep.common.response.dto.DataResponseDto;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.domain.Member;
import com.konkuk.chapterkeep.home.dto.HomeResDto;
import com.konkuk.chapterkeep.home.service.HomeService;
import com.konkuk.chapterkeep.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    private final HomeService homeService;
    private final MemberService memberService;

    @GetMapping
    public DataResponseDto<HomeResDto> getHomeData() {
        Member member = memberService.getCurrentMember();
        HomeResDto response = homeService.getHomeData(member);
        return new DataResponseDto<>(response, Code.OK, "홈 화면 데이터를 성공적으로 가져왔습니다.");
    }
}
