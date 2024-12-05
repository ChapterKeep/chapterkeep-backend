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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikesService {

    private final LikesRepository likesRepository;
    private final BookReviewRepository bookReviewRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final EssayPostRepository essayPostRepository;

    /*
    TODO : 경우에 따라 분기 처리 할 수 있게 리팩토링
    */

    public void bookReviewToggleLike(Member member, Long reviewId) {
        try {
            Long memberId = member.getMemberId();

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
                            member,
                            null
                    );
                    likesRepository.save(like);
                } catch (Exception e) {
                    throw new GeneralException(Code.DATABASE_ERROR, "좋아요 저장 중 오류 발생 : " + e.getMessage());
                }
            }
        } catch (GeneralException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralException(Code.FILE_UPLOAD_ERROR, "좋아요 상태 변경 도중 알 수 없는 오류 발생");
        }
    }


    public void postToggleLike(Member member, Long postId) {
        try {

            Long memberId = memberService.getCurrentMemberId();

            Post post = essayPostRepository.findById(postId)
                    .orElseThrow(() -> new GeneralException(Code.POST_NOT_FOUND));

            Optional<Likes> existingLike = likesRepository.findByMemberMemberIdAndPostPostId(memberId, postId);

            if (existingLike.isPresent()) {
                likesRepository.delete(existingLike.get());
            } else {
                Likes like = Likes.createLikes(
                        null,
                        post,
                        member,
                        null);
                likesRepository.save(like);
            }
        } catch (GeneralException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralException(Code.FILE_UPLOAD_ERROR, "좋아요 상태 변경 도중 알 수 없는 오류 발생");
        }
    }
}