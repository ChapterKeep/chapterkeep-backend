package com.konkuk.chapterkeep.bookReview.repository;

import com.konkuk.chapterkeep.domain.BookInfo;
import com.konkuk.chapterkeep.domain.BookReview;
import com.konkuk.chapterkeep.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Book;
import java.util.List;

public interface BookReviewRepository extends JpaRepository<BookReview, Long> {
    List<BookReview> findByMember_MemberId(Long memberId);
    List<BookReview> findByBookInfo_BookId(Long bookId);
    long countByMember_MemberId(Long memberId);
}
