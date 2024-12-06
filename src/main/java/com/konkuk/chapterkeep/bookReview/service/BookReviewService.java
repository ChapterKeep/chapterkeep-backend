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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookReviewService {

    private final BookInfoRepository bookInfoRepository;
    private final BookReviewRepository bookReviewRepository;
    private final LikesRepository likesRepository;

    public BookReviewCreateResDto saveBookReview(Member member, BookDto bookDto, BookReviewReqDto bookReviewReqDto) {

        try {
            // 필수 필드 검증
            validateBookReviewRequest(bookReviewReqDto);

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

        } catch (GeneralException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralException(Code.INTERNAL_ERROR, "독서기록 저장 도중 오류 발생: " + e.getMessage());
        }
    }


    public BookReviewResDto getBookReviewById(Member member, Long reviewId) {
        try {
            BookReview bookReview = bookReviewRepository.findById(reviewId)
                    .orElseThrow(() -> new GeneralException(Code.REVIEW_NOT_FOUND, "존재하지 않는 독서기록 : " + reviewId));

            // BookInfo가 존재하지 않을 경우
            if (bookReview.getBookInfo() == null) {
                throw new GeneralException(Code.NOT_FOUND, "책 정보가 없는 독서기록입니다.");
            }

            BookDto bookInfo = BookDto.builder()
                    .title(bookReview.getBookInfo().getTitle())
                    .writer(bookReview.getBookInfo().getWriter())
                    .coverUrl(bookReview.getBookInfo().getCoverUrl())
                    .build();

            // coverColor가 없을 경우 기본값 설정
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
        } catch (GeneralException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralException(Code.INTERNAL_ERROR, "독서기록 조회 도중 오류 발생: " + e.getMessage());
        }
    }

    public BookReviewUpdateResDto updateBookReview(Long reviewId, BookReviewReqDto bookReviewReqDto) {
        try{
            // 필수 필드 검증
            validateBookReviewRequest(bookReviewReqDto);

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
        } catch (GeneralException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralException(Code.INTERNAL_ERROR, "수정된 독서기록 저장 도중 오류 발생: " + e.getMessage());
        }
    }

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
            throw new GeneralException(Code.DATABASE_ERROR, "독서 기록 삭제 도중 오류 발생 : " + e.getMessage());
        }
    }

    private void validateBookReviewRequest(BookReviewReqDto bookReviewReqDto) {
        if (bookReviewReqDto.getReviewTitle() == null || bookReviewReqDto.getReviewTitle().trim().isEmpty()) {
            throw new GeneralException(Code.INVALID_INPUT_VALUE, "리뷰 제목은 필수 항목입니다.");
        }
        if (bookReviewReqDto.getRating() < 0 || bookReviewReqDto.getRating() > 5) {
            throw new GeneralException(Code.INVALID_INPUT_VALUE, "평점은 0에서 5 사이여야 합니다.");
        }
        if (bookReviewReqDto.getContent() == null || bookReviewReqDto.getContent().trim().isEmpty()) {
            throw new GeneralException(Code.INVALID_INPUT_VALUE, "리뷰 내용은 필수 항목입니다.");
        }
    }

}
