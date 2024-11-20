package com.konkuk.chapterkeep.likes.repository;

import com.konkuk.chapterkeep.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByMemberMemberIdAndBookReviewBookReviewId(Long memberId, Long bookReviewId);
    long countByBookReview_BookReviewId(Long bookReviewId);
}

