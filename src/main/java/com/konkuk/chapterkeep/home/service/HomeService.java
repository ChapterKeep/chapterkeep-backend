package com.konkuk.chapterkeep.home.service;

import com.konkuk.chapterkeep.bookReview.repository.BookReviewRepository;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.common.response.exception.GeneralException;
import com.konkuk.chapterkeep.domain.BookReview;
import com.konkuk.chapterkeep.domain.Member;
import com.konkuk.chapterkeep.home.dto.BookShelfResDto;
import com.konkuk.chapterkeep.home.dto.HomeResDto;
import com.konkuk.chapterkeep.home.dto.ProfileResDto;
import com.konkuk.chapterkeep.likes.repository.LikesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HomeService {

    private final BookReviewRepository bookReviewRepository;
    private final LikesRepository likesRepository;

    public HomeResDto getHomeData(Member member) {

        try {
            // 현재 사용자 ID 가져오기
            Long currentMemberId = member.getMemberId();

            ProfileResDto profileResDto = ProfileResDto.builder()
                    .nickname(member.getNickname())
                    .introduction(member.getIntroduction())
                    .profileUrl(member.getProfileUrl())
                    .visibility(member.getVisibility())
                    .postCount((long) member.getBookReviews().size())
                    .build();

            // 사용자 독서 기록 데이터
            List<BookReview> bookReviews = bookReviewRepository.findByMember_MemberId(currentMemberId);
            if (bookReviews.isEmpty()) {
                throw new GeneralException(Code.NOT_FOUND, "해당 사용자의 독서 기록이 존재하지 않음 : " + currentMemberId);
            }

            List<BookShelfResDto> bookShelfResDtoList = bookReviews.stream()
                    .map(bookReview -> BookShelfResDto.builder()
                            .reviewId(bookReview.getBookReviewId())
                            .coverUrl(bookReview.getBookInfo().getCoverUrl())
                            .build()
                    )
                    .toList();

            return HomeResDto.builder()
                    .profileResDto(profileResDto)
                    .bookShelfResDtoList(bookShelfResDtoList)
                    .build();

        } catch (GeneralException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralException(Code.INTERNAL_ERROR, "홈 데이터를 불러오는 중 오류 발생: " + e.getMessage());
        }
    }
}