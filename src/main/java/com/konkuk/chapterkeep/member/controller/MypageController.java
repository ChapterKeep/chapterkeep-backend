package com.konkuk.chapterkeep.member.controller;

import com.konkuk.chapterkeep.common.response.dto.DataResponseDto;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.member.dto.MypagePostListDto;
import com.konkuk.chapterkeep.member.dto.MypageResDto;
import com.konkuk.chapterkeep.member.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;

    @GetMapping("/mypage")
    public DataResponseDto<MypageResDto> getMypage(){
        MypageResDto mypageResDto = mypageService.getMyPage();
        return new DataResponseDto<>(mypageResDto, Code.OK);
    }

    @GetMapping("/mypage/myPosts")
    public DataResponseDto<List<MypagePostListDto>> getMyPosts(){
        List<MypagePostListDto> myPosts = mypageService.getMyPosts();
        return new DataResponseDto<>(myPosts, Code.OK);
    }

    @GetMapping("/mypage/commentedPosts")
    public DataResponseDto<List<MypagePostListDto>> getCommentedPosts(){
        List<MypagePostListDto> commentedPosts = mypageService.getCommentedPosts();
        return new DataResponseDto<>(commentedPosts, Code.OK);
    }
    @GetMapping("/mypage/likedPosts")
    public DataResponseDto<List<MypagePostListDto>> getLikedPosts(){
        List<MypagePostListDto> likedPosts = mypageService.getLikedPosts();
        return new DataResponseDto<>(likedPosts, Code.OK);
    }
}
