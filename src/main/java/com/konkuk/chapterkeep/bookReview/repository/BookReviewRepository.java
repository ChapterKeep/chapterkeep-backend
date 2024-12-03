package com.konkuk.chapterkeep.bookReview.repository;

import com.konkuk.chapterkeep.domain.BookReview;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookReviewRepository extends JpaRepository<BookReview, Long> {
    List<BookReview> findByMember_MemberId(Long memberId);
    List<BookReview> findByBookInfo_BookId(Long bookId);
    long countByMember_MemberId(Long memberId);
    long countByBookInfo_BookId(Long bookId);

    @Query("SELECT br.bookInfo.bookId " +
            "FROM BookReview br " +
            "GROUP BY br.bookInfo.bookId " +
            "ORDER BY COUNT(br.bookInfo.bookId) DESC")
    List<Long> findBookIdsOrderByReviewCountDesc(Pageable pageable);
}
