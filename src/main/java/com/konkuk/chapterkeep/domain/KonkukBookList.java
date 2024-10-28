package com.konkuk.chapterkeep.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "KonkukBookList")
@Getter
@Setter
public class KonkukBookList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "konkuk_book_list_id")
    private Long konkukBookListId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "writer", nullable = false)
    private String writer;

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "library_url", nullable = false)
    private String libraryUrl;

    @Column(name = "rental_count", nullable = false)
    private int rentalCount;

}
