package com.konkuk.chapterkeep.member.controller;

import com.konkuk.chapterkeep.common.response.dto.DataResponseDto;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/id-test")  // 임시 로그인 테스트 api
    public DataResponseDto<Long> idTest() {
        return new DataResponseDto<>(memberService.getCurrentMemberId(), Code.OK,"사용자 고유id 불러오기 성공");
    }
    @GetMapping("/role-test")  // 임시 로그인 테스트 api
    public DataResponseDto<String> roleTest() {
        return new DataResponseDto<>(memberService.getCurrentMemberRole().toString(), Code.OK,"사용자 role 불러오기 성공");
    }


}
