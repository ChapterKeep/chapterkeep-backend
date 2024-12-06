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
    private final EssayPostRepository essayPostRepository;

    public String bookReviewToggleLike(Member member, Long reviewId) {
        try {
            String response;
            Long memberId = member.getMemberId();

            BookReview bookReview = bookReviewRepository.findById(reviewId)
                    .orElseThrow(() -> new GeneralException(Code.REVIEW_NOT_FOUND, "존재하지 않는 독서 기록 : " + reviewId));

            Optional<Likes> existingLike = likesRepository.findByMemberMemberIdAndBookReviewBookReviewId(memberId, reviewId);

            if (existingLike.isPresent()) {
                try {
                    likesRepository.delete(existingLike.get());
                    response = "좋아요 삭제 완료";
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
                    response = "좋아요 저장 완료";
                } catch (Exception e) {
                    throw new GeneralException(Code.DATABASE_ERROR, "좋아요 저장 중 오류 발생 : " + e.getMessage());
                }
            }
            return response;
        } catch (GeneralException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralException(Code.INTERNAL_ERROR, "좋아요 상태 변경 도중 알 수 없는 오류 발생");
        }
    }


    public String postToggleLike(Member member, Long postId) {
        try {
            String response;
            Long memberId = memberService.getCurrentMemberId();

            Post post = essayPostRepository.findById(postId)
                    .orElseThrow(() -> new GeneralException(Code.POST_NOT_FOUND));

            Optional<Likes> existingLike = likesRepository.findByMemberMemberIdAndPostPostId(memberId, postId);

            if (existingLike.isPresent()) {
                likesRepository.delete(existingLike.get());
                response = "좋아요 삭제 완료";
            } else {
                Likes like = Likes.createLikes(
                        null,
                        post,
                        member,
                        null);
                likesRepository.save(like);
                response = "좋아요 저장 완료";
            }
            return response;
        } catch (GeneralException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralException(Code.INTERNAL_ERROR, "좋아요 상태 변경 도중 알 수 없는 오류 발생");
        }
    }
}