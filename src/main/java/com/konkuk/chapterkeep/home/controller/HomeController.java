package com.konkuk.chapterkeep.home.controller;

import com.konkuk.chapterkeep.common.response.dto.DataResponseDto;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.home.dto.HomeResDto;
import com.konkuk.chapterkeep.home.dto.ProfileResDto;
import com.konkuk.chapterkeep.home.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    private final HomeService homeService;

    @GetMapping
    public DataResponseDto<HomeResDto> getBookReviews() {
        HomeResDto response = homeService.getBookReviews();
        return new DataResponseDto<>(response, Code.OK, "홈 화면 데이터를 성공적으로 가져왔습니다.");
    }

    @GetMapping("/profile")
    public DataResponseDto<ProfileResDto> getProfile(){
        ProfileResDto profileResDto = homeService.getProfile();
        return new DataResponseDto<>(profileResDto, Code.OK, "프로필 데이터를 성공적으로 가져왔습니다.");
    }
}
