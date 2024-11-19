package com.konkuk.chapterkeep.bookInfo.repository;

import com.konkuk.chapterkeep.domain.BookInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookInfoRepository extends JpaRepository<BookInfo, Long> {
    Optional<BookInfo> findByTitleAndWriter(String title, String writer);
}
