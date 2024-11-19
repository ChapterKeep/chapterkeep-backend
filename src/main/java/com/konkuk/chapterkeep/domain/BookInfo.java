package com.konkuk.chapterkeep.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book_info")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "title", nullable = false, length = 70)
    private String title;

    @Column(name = "writer", nullable = false, length = 20)
    private String writer;

    @Column(name = "coverUrl", nullable = false)
    private String coverUrl;

    @Builder
    public BookInfo(String title, String writer, String coverUrl) {
        this.title = title;
        this.writer = writer;
        this.coverUrl = coverUrl;
    }
}
