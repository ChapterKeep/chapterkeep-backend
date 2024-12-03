package com.konkuk.chapterkeep.likes.service;

import com.konkuk.chapterkeep.bookReview.repository.BookReviewRepository;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.common.response.exception.GeneralException;
import com.konkuk.chapterkeep.domain.BookReview;
import com.konkuk.chapterkeep.domain.Likes;
import com.konkuk.chapterkeep.domain.Member;
import com.konkuk.chapterkeep.domain.Post;
import com.konkuk.chapterkeep.likes.repository.LikesRepository;
import com.konkuk.chapterkeep.member.repository.MemberRepository;
import com.konkuk.chapterkeep.member.service.MemberService;
import com.konkuk.chapterkeep.post.repository.EssayPostRepository;
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
    private final EssayPostRepository essayPostRepository;

    /*
    TODO : 경우에 따라 분기 처리 할 수 있게 리팩토링
    */

    public void bookReviewToggleLike(Long reviewId) {
        Long memberId = memberService.getCurrentMemberId();
        Optional<Member> member = memberRepository.findById(memberId);

        BookReview bookReview = bookReviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(Code.REVIEW_NOT_FOUND, "존재하지 않는 독서 기록 : " + reviewId));

        Optional<Likes> existingLike = likesRepository.findByMemberMemberIdAndBookReviewBookReviewId(memberId, reviewId);

        if (existingLike.isPresent()) {
            try {
                likesRepository.delete(existingLike.get());
            } catch (Exception e) {
                throw new GeneralException(Code.DATABASE_ERROR, "좋아요 삭제 중 오류 발생 : " + e.getMessage());
            }
        } else {
            try {
                Likes like = Likes.createLikes(
                        bookReview,
                        null,
                        member.orElseThrow(() -> new GeneralException(Code.MEMBER_NOT_FOUND, "존재하지 않는 회원 : " + memberId)),
                        null
                );
                likesRepository.save(like);
            } catch (Exception e) {
                throw new GeneralException(Code.DATABASE_ERROR, "좋아요 저장 중 오류 발생 : " + e.getMessage());
            }
        }
    }


    public void postToggleLike(Long postId) {

        Long memberId = memberService.getCurrentMemberId();
        Optional<Member> member = memberRepository.findById(memberId);

        Post post = essayPostRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(Code.POST_NOT_FOUND));

        Optional<Likes> existingLike = likesRepository.findByMemberMemberIdAndPostPostId(memberId, postId);

        if (existingLike.isPresent()) {
            likesRepository.delete(existingLike.get());
        } else {
            Likes like = Likes.createLikes(
                    null,
                    post,
                    member.orElse(null),null);
            likesRepository.save(like);
        }
    }
}