package com.konkuk.chapterkeep.member.controller;

import com.konkuk.chapterkeep.common.response.dto.DataResponseDto;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.member.dto.MypageResDto;
import com.konkuk.chapterkeep.member.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;

    @GetMapping("/mypage")
    public DataResponseDto<MypageResDto> getMypage(){
        MypageResDto mypageResDto = mypageService.getMyPage();
        return new DataResponseDto<>(mypageResDto, Code.OK);
    }
}
