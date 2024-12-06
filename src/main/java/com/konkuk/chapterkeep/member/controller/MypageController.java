package com.konkuk.chapterkeep.member.controller;

import com.konkuk.chapterkeep.common.response.dto.DataResponseDto;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.domain.Member;
import com.konkuk.chapterkeep.member.dto.MypagePostListDto;
import com.konkuk.chapterkeep.member.dto.MypageResDto;
import com.konkuk.chapterkeep.member.service.MemberService;
import com.konkuk.chapterkeep.member.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;
    private final MemberService memberService;

    @GetMapping("/mypage")
    public DataResponseDto<MypageResDto> getMypage(){
        Member member = memberService.getCurrentMember();
        MypageResDto mypageResDto = mypageService.getMyPage(member);
        return new DataResponseDto<>(mypageResDto, Code.OK);
    }

    @GetMapping("/mypage/myPosts")
    public DataResponseDto<List<MypagePostListDto>> getMyPosts(){
        Member member = memberService.getCurrentMember();
        List<MypagePostListDto> myPosts = mypageService.getMyPosts(member);
        return new DataResponseDto<>(myPosts, Code.OK);
    }

    @GetMapping("/mypage/commentedPosts")
    public DataResponseDto<List<MypagePostListDto>> getCommentedPosts(){
        Member member = memberService.getCurrentMember();
        List<MypagePostListDto> commentedPosts = mypageService.getCommentedPosts(member);
        return new DataResponseDto<>(commentedPosts, Code.OK);
    }
    @GetMapping("/mypage/likedPosts")
    public DataResponseDto<List<MypagePostListDto>> getLikedPosts(){
        Member member = memberService.getCurrentMember();
        List<MypagePostListDto> likedPosts = mypageService.getLikedPosts(member);
        return new DataResponseDto<>(likedPosts, Code.OK);
    }
}
