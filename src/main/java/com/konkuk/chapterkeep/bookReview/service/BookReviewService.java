package com.konkuk.chapterkeep.bookReview.service;


import com.konkuk.chapterkeep.bookInfo.repository.BookInfoRepository;
import com.konkuk.chapterkeep.bookReview.dto.*;
import com.konkuk.chapterkeep.bookReview.repository.BookReviewRepository;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.common.response.exception.GeneralException;
import com.konkuk.chapterkeep.domain.BookInfo;
import com.konkuk.chapterkeep.domain.BookReview;
import com.konkuk.chapterkeep.domain.Member;
import com.konkuk.chapterkeep.domain.enums.CoverColor;
import com.konkuk.chapterkeep.likes.repository.LikesRepository;
import com.konkuk.chapterkeep.member.repository.MemberRepository;
import com.konkuk.chapterkeep.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookReviewService {

    private final BookInfoRepository bookInfoRepository;
    private final BookReviewRepository bookReviewRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;

    public BookReviewCreateResDto saveBookReview(BookDto bookDto, BookReviewReqDto bookReviewReqDto) {

        Long memberId = memberService.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(Code.MEMBER_NOT_FOUND, "존재하지 않는 회원 : " + memberId));

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

        // BookReview 저장
        BookReview bookReview = BookReview.createBookReview(
                member,
                bookReviewReqDto.getReviewTitle(),
                bookReviewReqDto.getRating(),
                bookReviewReqDto.getQuotation(),
                bookReviewReqDto.getContent(),
                CoverColor.valueOf(coverColor),
                bookInfo
        );
        bookReviewRepository.save(bookReview);

        return BookReviewCreateResDto.builder()
                .reviewId(bookReview.getBookReviewId())
                .createdAt(bookReview.getCreatedAt())
                .build();
    }

    public BookReviewResDto getBookReviewById(Long reviewId) {

        Long memberId = memberService.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(Code.MEMBER_NOT_FOUND, "존재하지 않는 회원 : " + memberId));

        BookReview bookReview = bookReviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(Code.REVIEW_NOT_FOUND, "존재하지 않는 독서기록 : " + reviewId));

        BookDto bookInfo = BookDto.builder()
                .title(bookReview.getBookInfo().getTitle())
                .writer(bookReview.getBookInfo().getWriter())
                .coverUrl(bookReview.getBookInfo().getCoverUrl())
                .build();

        String coverColor = bookReview.getCoverColor() != null
                ? bookReview.getCoverColor().name()
                : "#FFFFFF";

        return BookReviewResDto.builder()
                .bookInfo(bookInfo)
                .reviewId(bookReview.getBookReviewId())
                .reviewTitle(bookReview.getReviewTitle())
                .rating(bookReview.getRating())
                .quotation(bookReview.getQuotation())
                .content(bookReview.getContent())
                .coverColor(coverColor)
                .createdAt(bookReview.getCreatedAt())
                .modifiedAt(bookReview.getModifiedAt())
                .nickname(member.getNickname())
                .likesCount(likesRepository.countByBookReview_BookReviewId(reviewId))
                .build();
    }

    public BookReviewUpdateResDto updateBookReview(Long reviewId, BookReviewReqDto bookReviewReqDto) {

        BookReview bookReview = bookReviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(Code.REVIEW_NOT_FOUND, "존재하지 않는 독서기록 : " + reviewId));

        CoverColor coverColor = null;
        if (bookReviewReqDto.getCoverColor() != null) {
            try {
                coverColor = CoverColor.valueOf(bookReviewReqDto.getCoverColor().toUpperCase());
            } catch (GeneralException e) {
                throw new GeneralException(Code.INVALID_INPUT_VALUE, "유효하지 않은 컬러 값 : " + bookReviewReqDto.getCoverColor());
            }
        }

        bookReview.update(
                bookReviewReqDto.getReviewTitle(),
                bookReviewReqDto.getRating(),
                bookReviewReqDto.getQuotation(),
                bookReviewReqDto.getContent(),
                coverColor != null ? coverColor : bookReview.getCoverColor()
        );
        bookReviewRepository.save(bookReview);

        return BookReviewUpdateResDto.builder()
                .reviewId(bookReview.getBookReviewId())
                .modifiedAt(bookReview.getModifiedAt())
                .build();
    }

    @Transactional
    public void deleteBookReview(Long reviewId) {
        if (!bookReviewRepository.existsById(reviewId)) {
            throw new GeneralException(Code.REVIEW_NOT_FOUND, "존재하지 않는 독서 기록 : " + reviewId);
        }

        if (likesRepository.existsByBookReview_BookReviewId(reviewId)) {
            try {
                likesRepository.deleteByBookReview_BookReviewId(reviewId);
            } catch (Exception e) {
                throw new GeneralException(Code.DATABASE_ERROR, "좋아요 데이터 삭제 중 오류 발생 : " + e.getMessage());
            }
        }
        try {
            bookReviewRepository.deleteById(reviewId);
        } catch (Exception e) {
            throw new GeneralException(Code.DATABASE_ERROR, "독서 기록 삭제 중 오류 발생 : " + e.getMessage());
        }
    }
}
