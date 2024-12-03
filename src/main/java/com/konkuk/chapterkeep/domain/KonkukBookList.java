package com.konkuk.chapterkeep.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "konkuk_book_list")
@Getter
public class KonkukBookList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "konkuk_book_list_id")
    private Long konkukBookListId;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "department", nullable = false, length = 25)
    private String department;

    @Column(name = "library_url", nullable = false)
    private String libraryUrl;

    @Column(name = "rental_count", nullable = false)
    private int rentalCount;

}
