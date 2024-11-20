package com.konkuk.chapterkeep.bookReview.dto;

import com.konkuk.chapterkeep.domain.BookInfo;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private String title;
    private String writer;
    private String coverUrl;

    public static BookDto fromEntity(BookInfo bookInfo) {
        return BookDto.builder()
                .title(bookInfo.getTitle())
                .writer(bookInfo.getWriter())
                .coverUrl(bookInfo.getCoverUrl())
                .build();
    }
}
