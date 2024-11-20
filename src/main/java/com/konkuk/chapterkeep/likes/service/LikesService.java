package com.konkuk.chapterkeep.likes.service;

import com.konkuk.chapterkeep.bookReview.repository.BookReviewRepository;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.common.response.exception.GeneralException;
import com.konkuk.chapterkeep.domain.BookReview;
import com.konkuk.chapterkeep.domain.Likes;
import com.konkuk.chapterkeep.domain.Member;
import com.konkuk.chapterkeep.likes.repository.LikesRepository;
import com.konkuk.chapterkeep.member.repository.MemberRepository;
import com.konkuk.chapterkeep.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final BookReviewRepository bookReviewRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    public void toggleLike(Long bookReviewId) {

        Long memberId = memberService.getCurrentMemberId();
        Optional<Member> member = memberRepository.findById(memberId);

        BookReview bookReview = bookReviewRepository.findById(bookReviewId)
                .orElseThrow(() -> new GeneralException(Code.REVIEW_NOT_FOUND));

        Optional<Likes> existingLike = likesRepository.findByMemberMemberIdAndBookReviewBookReviewId(memberId, bookReviewId);

        if (existingLike.isPresent()) {
            likesRepository.delete(existingLike.get());
        } else {
            Likes like = Likes.createLikes(
                    bookReview,
                    null,
                    member.orElse(null),null);
            likesRepository.save(like);
        }
    }

    public long getLikesCountForReview(Long bookReviewId) {
        // 해당 bookReviewId에 대한 좋아요 개수를 카운트
        return likesRepository.countByBookReview_BookReviewId(bookReviewId);
    }
}