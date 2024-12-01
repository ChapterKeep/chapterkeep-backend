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
import com.konkuk.chapterkeep.post.repository.PostRepository;
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
    private final PostRepository postRepository;

    /*
    TODO : 경우에 따라 분기 처리 할 수 있게 리팩토링
    */

    public void bookReviewToggleLike(Long bookReviewId) {

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

    public void postToggleLike(Long postId) {

        Long memberId = memberService.getCurrentMemberId();
        Optional<Member> member = memberRepository.findById(memberId);

        Post post = postRepository.findById(postId)
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