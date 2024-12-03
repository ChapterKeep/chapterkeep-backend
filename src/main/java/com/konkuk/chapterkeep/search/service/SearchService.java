package com.konkuk.chapterkeep.search.service;

import com.konkuk.chapterkeep.bookInfo.repository.BookInfoRepository;
import com.konkuk.chapterkeep.bookReview.dto.BookReviewResDto;
import com.konkuk.chapterkeep.bookReview.repository.BookReviewRepository;
import com.konkuk.chapterkeep.domain.BookInfo;
import com.konkuk.chapterkeep.domain.BookReview;
import com.konkuk.chapterkeep.domain.Member;
import com.konkuk.chapterkeep.likes.repository.LikesRepository;
import com.konkuk.chapterkeep.member.repository.MemberRepository;
import com.konkuk.chapterkeep.search.dto.SearchBookReviewResDto;
import com.konkuk.chapterkeep.search.dto.SearchBookShelfResDto;
import com.konkuk.chapterkeep.search.dto.ShelfInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final BookReviewRepository bookReviewRepository;
    private final BookInfoRepository bookInfoRepository;
    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;

    public SearchBookReviewResDto getReviewsByTitle(String title) {

        List<BookInfo> bookInfoList = bookInfoRepository.findByTitleContaining(title);
        List<Long> bookIds = bookInfoList.stream()
                .map(BookInfo::getBookId)
                .toList();
        List<BookReview> allBookReviews = bookIds.stream()
                .flatMap(bookId -> bookReviewRepository.findByBookInfo_BookId(bookId).stream())
                .toList();
        List<BookReviewResDto> result = allBookReviews.stream()
                .map(bookReview -> BookReviewResDto.fromEntity(bookReview, likesRepository.countByBookReview_BookReviewId(bookReview.getBookReviewId())))
                .collect(Collectors.toList());

        return SearchBookReviewResDto.builder()
                .bookReviews(result)
                .build();
    }

    public SearchBookShelfResDto getBookShelfByNickName(String nickname) {

        List<Member> members = memberRepository.findByNicknameContaining(nickname);
        List<ShelfInfoDto> result = members.stream()
                .map(member -> {

                    long bookReviewCount = bookReviewRepository.countByMember_MemberId(member.getMemberId());

                    return new ShelfInfoDto(
                            member.getNickname(),
                            member.getProfileUrl(),
                            bookReviewCount
                    );
                })
                .collect(Collectors.toList());

        return SearchBookShelfResDto.builder()
                .shelfInfoList(result)
                .build();
    }
}
