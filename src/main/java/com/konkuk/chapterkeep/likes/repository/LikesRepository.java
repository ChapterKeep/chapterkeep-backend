package com.konkuk.chapterkeep.likes.repository;

import com.konkuk.chapterkeep.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    boolean existsByBookReview_BookReviewId(Long bookReviewId);
    void deleteByBookReview_BookReviewId(Long bookReviewId);
    boolean existsByPost_PostId(Long postId);
    void deleteByPost_PostId(Long postId);
    Optional<Likes> findByMemberMemberIdAndBookReviewBookReviewId(Long memberId, Long bookReviewId);
    Optional<Likes> findByMemberMemberIdAndPostPostId(Long memberId, Long postId);
    long countByBookReview_BookReviewId(Long bookReviewId);
    long countByPost_PostId(Long postId);

    @Query("SELECT l.post.postId " +
            "FROM Likes l " +
            "GROUP BY l.post.postId " +
            "ORDER BY COUNT(l.post.postId) DESC")
    List<Long> findTop3PostIdsByLikesCount();
}

