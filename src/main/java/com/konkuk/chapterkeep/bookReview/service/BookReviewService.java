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
import com.konkuk.chapterkeep.domain.Likes;
import com.konkuk.chapterkeep.domain.Member;
import com.konkuk.chapterkeep.domain.enums.CoverColor;
import com.konkuk.chapterkeep.home.dto.HomeResDto;
import com.konkuk.chapterkeep.likes.repository.LikesRepository;
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
    private final LikesRepository likesRepository;

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

        return BookReviewResDto.builder()
                .bookInfo(BookDto.builder()
                        .title(bookInfo.getTitle())
                        .writer(bookInfo.getWriter())
                        .coverUrl(bookInfo.getCoverUrl())
                        .build())
                .reviewTitle(bookReview.getReviewTitle())
                .rating(bookReview.getRating())
                .quotation(bookReview.getQuotation())
                .content(bookReview.getContent())
                .coverColor(bookReview.getCoverColor().name())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .nickname(member.getNickname())
                .build();
    }

    public BookReviewResDto getBookReviewById(Long reviewId) {

        Long memberId = memberService.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(Code.MEMBER_NOT_FOUND, "멤버를 찾을 수 없습니다: " + memberId));

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
                .reviewTitle(bookReview.getReviewTitle())
                .rating(bookReview.getRating())
                .quotation(bookReview.getQuotation())
                .content(bookReview.getContent())
                .coverColor(coverColor)
                .createdAt(bookReview.getCreatedDate())
                .modifiedAt(bookReview.getModifiedDate())
                .nickname(member.getNickname())
                .likesCount(likesRepository.countByBookReview_BookReviewId(reviewId))
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
                bookReviewReqDto.getReviewTitle(),
                bookReviewReqDto.getRating(),
                bookReviewReqDto.getQuotation(),
                bookReviewReqDto.getContent(),
                coverColor != null ? coverColor : bookReview.getCoverColor()
        );
        bookReviewRepository.save(bookReview);
        long likesCount = likesRepository.countByBookReview_BookReviewId(bookReview.getBookReviewId());

        return BookReviewResDto.fromEntity(bookReview, likesCount);
    }

    // TODO : 좋아요 눌린 독서 기록이 삭제되지 않는 오류 - 독서 기록 삭제 이전에 해당 독서 기록에 눌린 좋아요 엔티티 먼저 삭제 (cascade)
    // 양방향으로 전환 or 독서 기록 삭제 이전에 좋아요 엔티티 먼저 삭제 되도록 코드 상에서 작성
    public void deleteBookReview(Long reviewId) {
        if (likesRepository.existsByBookReview_BookReviewId(reviewId)) {
            likesRepository.deleteByBookReview_BookReviewId(reviewId);
        }
        bookReviewRepository.deleteById(reviewId);
    }
}