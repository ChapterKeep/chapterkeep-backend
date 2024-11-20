package com.konkuk.chapterkeep.bookReview.service;


import com.konkuk.chapterkeep.bookInfo.repository.BookInfoRepository;
import com.konkuk.chapterkeep.bookReview.dto.BookDto;
import com.konkuk.chapterkeep.bookReview.dto.BookReviewReqDto;
import com.konkuk.chapterkeep.bookReview.dto.BookReviewResDto;
import com.konkuk.chapterkeep.bookReview.repository.BookReviewRepository;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.common.response.exception.GeneralException;
import com.konkuk.chapterkeep.domain.BookInfo;
import com.konkuk.chapterkeep.domain.BookReview;
import com.konkuk.chapterkeep.domain.Member;
import com.konkuk.chapterkeep.domain.enums.CoverColor;
import com.konkuk.chapterkeep.home.dto.HomeResDto;
import com.konkuk.chapterkeep.member.repository.MemberRepository;
import com.konkuk.chapterkeep.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookReviewService {

    private final BookInfoRepository bookInfoRepository;
    private final BookReviewRepository bookReviewRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    public BookReviewResDto saveBookReview(BookDto bookDto, BookReviewReqDto bookReviewReqDto) {

        Long memberId = memberService.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(Code.MEMBER_NOT_FOUND, "멤버를 찾을 수 없습니다: " + memberId));

        // BookInfo 저장 또는 중복 체크
        BookInfo bookInfo = bookInfoRepository.findByTitleAndWriter(bookDto.getTitle(), bookDto.getWriter())
                .orElseGet(() -> bookInfoRepository.save(
                        BookInfo.builder()
                                .title(bookDto.getTitle())
                                .writer(bookDto.getWriter())
                                .coverUrl(bookDto.getCoverUrl())
                                .build()
                ));

        // coverColor가 null일 경우 기본값 설정
        String coverColor = bookReviewReqDto.getCoverColor() != null
                ? bookReviewReqDto.getCoverColor().toUpperCase()
                : "WHITE";

        // 중복 확인
        boolean exists = bookReviewRepository.existsByMemberAndBookInfo(member, bookInfo);
        if (exists) {
            throw new GeneralException(Code.DUPLICATE_REVIEW);
        }

        // BookReview 저장
        BookReview bookReview = BookReview.createBookReview(
                member,
                bookReviewReqDto.getRating(),
                bookReviewReqDto.getQuotation(),
                bookReviewReqDto.getContent(),
                CoverColor.valueOf(coverColor),
                bookInfo
        );
        bookReviewRepository.save(bookReview);

        return BookReviewResDto.builder()
                .bookInfo(BookDto.builder()
                        .title(bookInfo.getTitle())
                        .writer(bookInfo.getWriter())
                        .coverUrl(bookInfo.getCoverUrl())
                        .build())
                .rating(bookReview.getRating())
                .quotation(bookReview.getQuotation())
                .content(bookReview.getContent())
                .coverColor(bookReview.getCoverColor().name())
                .build();
    }

    public HomeResDto getBookReviews() {
        Long memberId = memberService.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(Code.MEMBER_NOT_FOUND, "멤버를 찾을 수 없습니다: " + memberId));

        List<BookReview> bookReviews = bookReviewRepository.findByMember_MemberId(memberId);

        List<BookReviewResDto> bookReviewResDtoList = bookReviews.stream()
                .map(BookReviewResDto::fromEntity)
                .toList();

        return HomeResDto.builder()
                .bookReviews(bookReviewResDtoList)
                .build();

    }

    public BookReviewResDto getBookReviewById(Long reviewId) {

        BookReview bookReview = bookReviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Book review not found with id: " + reviewId));

        BookDto bookInfo = BookDto.builder()
                .title(bookReview.getBookInfo().getTitle())
                .writer(bookReview.getBookInfo().getWriter())
                .coverUrl(bookReview.getBookInfo().getCoverUrl())
                .build();

        String coverColor = bookReview.getCoverColor() != null
                ? bookReview.getCoverColor().name()
                : "#FFFFFF";

        return BookReviewResDto.builder()
                .reviewId(reviewId)
                .bookInfo(bookInfo)
                .rating(bookReview.getRating())
                .quotation(bookReview.getQuotation())
                .content(bookReview.getContent())
                .coverColor(coverColor)
                .createdAt(bookReview.getCreatedDate())
                .updatedAt(bookReview.getModifiedDate())
                .build();
    }

    public BookReviewResDto updateBookReview(Long reviewId, BookReviewReqDto bookReviewReqDto) {

        BookReview bookReview = bookReviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Book review not found with id: " + reviewId));

        CoverColor coverColor = null;
        if (bookReviewReqDto.getCoverColor() != null) {
            try {
                coverColor = CoverColor.valueOf(bookReviewReqDto.getCoverColor().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid coverColor value: " + bookReviewReqDto.getCoverColor());
            }
        }

        bookReview.update(
                bookReviewReqDto.getRating(),
                bookReviewReqDto.getQuotation(),
                bookReviewReqDto.getContent(),
                coverColor != null ? coverColor : bookReview.getCoverColor()
        );
        bookReviewRepository.save(bookReview);

        return BookReviewResDto.fromEntity(bookReview);
    }

    public void deleteBookReview(Long reviewId) {

        BookReview bookReview = bookReviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Book review not found with id: " + reviewId));
        bookReviewRepository.delete(bookReview);
    }
}
