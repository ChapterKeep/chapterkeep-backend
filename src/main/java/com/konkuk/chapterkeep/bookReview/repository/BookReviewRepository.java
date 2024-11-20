package com.konkuk.chapterkeep.bookReview.repository;

import com.konkuk.chapterkeep.domain.BookInfo;
import com.konkuk.chapterkeep.domain.BookReview;
import com.konkuk.chapterkeep.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookReviewRepository extends JpaRepository<BookReview, Long> {
    boolean existsByMemberAndBookInfo(Member member, BookInfo bookInfo);
    List<BookReview> findByMember_MemberId(Long memberId);
}
