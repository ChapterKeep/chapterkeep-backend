package com.konkuk.chapterkeep.bookReview.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private String title;
    private String writer;
    private String coverUrl;

}
