package com.konkuk.chapterkeep.home.service;

import com.konkuk.chapterkeep.bookReview.dto.BookReviewResDto;
import com.konkuk.chapterkeep.bookReview.repository.BookReviewRepository;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.common.response.exception.GeneralException;
import com.konkuk.chapterkeep.domain.BookReview;
import com.konkuk.chapterkeep.domain.Member;
import com.konkuk.chapterkeep.home.dto.HomeResDto;
import com.konkuk.chapterkeep.home.dto.ProfileResDto;
import com.konkuk.chapterkeep.likes.repository.LikesRepository;
import com.konkuk.chapterkeep.member.repository.MemberRepository;
import com.konkuk.chapterkeep.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final MemberService memberService;
    private final BookReviewRepository bookReviewRepository;
    private final LikesRepository likesRepository;

    public HomeResDto getBookReviews() {

        List<BookReview> bookReviews = bookReviewRepository.findByMember_MemberId(memberService.getCurrentMemberId());

        List<BookReviewResDto> bookReviewResDtoList = bookReviews.stream()
                .map(bookReview -> {
                    long likesCount = likesRepository.countByBookReview_BookReviewId(bookReview.getBookReviewId());
                    return BookReviewResDto.fromEntity(bookReview, likesCount);
                })
                .toList();

        return HomeResDto.builder()
                .bookReviews(bookReviewResDtoList)
                .build();
    }

    public ProfileResDto getProfile(){
        Member member = memberService.getCurrentMember();

        ProfileResDto response;
        try {
            response = ProfileResDto.builder()
                    .nickname(member.getNickname())
                    .introduction(member.getIntroduction())
                    .profileUrl(member.getProfileUrl())
                    .visibility(member.getVisibility())
                    .postCount((long) member.getPosts().size())
                    .build();
        } catch (Exception e){
            throw new GeneralException(Code.INTERNAL_ERROR,"프로필 정보를 가져오는 도중 오류가 발생했습니다.");
        }

        return response;
    }
}