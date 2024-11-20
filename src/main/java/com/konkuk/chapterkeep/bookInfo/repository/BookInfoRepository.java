package com.konkuk.chapterkeep.bookInfo.repository;

import com.konkuk.chapterkeep.domain.BookInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.awt.print.Book;
import java.util.List;
import java.util.Optional;

public interface BookInfoRepository extends JpaRepository<BookInfo, Long> {
    Optional<BookInfo> findByTitleAndWriter(String title, String writer);
    List<BookInfo> findByTitleContaining(String title);
}
