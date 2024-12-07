package com.konkuk.chapterkeep.bookReview.repository;

import com.konkuk.chapterkeep.domain.BookReview;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookReviewRepository extends JpaRepository<BookReview, Long> {
    List<BookReview> findByMember_MemberId(Long memberId);
    long countByMember_MemberId(Long memberId);

    @Query("SELECT br.bookInfo.bookId " +
            "FROM BookReview br " +
            "GROUP BY br.bookInfo.bookId " +
            "ORDER BY COUNT(br.bookInfo.bookId) DESC")
    List<Long> findBookIdsOrderByReviewCountDesc(Pageable pageable);

    @Query("SELECT br FROM BookReview br WHERE br.bookInfo.bookId = :bookId AND br.member.visibility = true ORDER BY br.createdAt DESC")
    List<BookReview> findByBookInfo_BookIdAndMemberVisibility(@Param("bookId") Long bookId);
}
