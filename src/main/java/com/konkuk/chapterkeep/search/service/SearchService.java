package com.konkuk.chapterkeep.search.service;

import com.konkuk.chapterkeep.bookInfo.repository.BookInfoRepository;
import com.konkuk.chapterkeep.bookReview.repository.BookReviewRepository;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.common.response.exception.GeneralException;
import com.konkuk.chapterkeep.domain.BookInfo;
import com.konkuk.chapterkeep.domain.Member;
import com.konkuk.chapterkeep.member.repository.MemberRepository;
import com.konkuk.chapterkeep.search.dto.SearchBookReviewResDto;
import com.konkuk.chapterkeep.search.dto.SearchBookShelfResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchService {

    private final BookReviewRepository bookReviewRepository;
    private final BookInfoRepository bookInfoRepository;
    private final MemberRepository memberRepository;

    public List<SearchBookReviewResDto> getReviewsByTitle(String title) {
        try {

            if (title == null || title.trim().isEmpty()) {
                throw new GeneralException(Code.INVALID_INPUT_VALUE, "비어있는 제목 검색어");
            }

            List<BookInfo> bookInfoList = bookInfoRepository.findByTitleContaining(title);

            return bookInfoList.stream()
                    .flatMap(bookInfo -> bookReviewRepository.findByBookInfo_BookId(bookInfo.getBookId()).stream()
                            .map(bookReview -> SearchBookReviewResDto.builder()
                                    .reviewId(bookReview.getBookReviewId())
                                    .reviewTitle(bookReview.getReviewTitle())
                                    .coverUrl(bookInfo.getCoverUrl())
                                    .nickname(bookReview.getMember().getNickname())
                                    .build()
                            )
                    )
                    .toList();

        }catch (GeneralException e){
            throw e;
        }catch (Exception e){
            throw new GeneralException(Code.INTERNAL_ERROR, "독서기록 조회 도중 알 수 없는 오류 발생");
        }
    }

    public List<SearchBookShelfResDto> getBookShelfByNickName(String nickname) {
        try {
            if (nickname == null || nickname.trim().isEmpty()) {
                throw new GeneralException(Code.INVALID_INPUT_VALUE, "비어있는 닉네임 검색어");
            }

            List<Member> members = memberRepository.findByNicknameContaining(nickname);

            return members.stream()
                    .filter(member -> Boolean.TRUE.equals(member.getVisibility()))
                    .map(member -> {
                        long bookReviewCount = bookReviewRepository.countByMember_MemberId(member.getMemberId());
                        return SearchBookShelfResDto.builder()
                                .memberId(member.getMemberId())
                                .nickname(member.getNickname())
                                .profileUrl(member.getProfileUrl())
                                .bookReviewCount(bookReviewCount)
                                .build();
                    })
                    .toList();
        }catch (GeneralException e){
            throw e;
        }catch (Exception e){
            throw new GeneralException(Code.INTERNAL_ERROR, "책장 조회 도중 알 수 없는 오류 발생");
        }
    }
}
