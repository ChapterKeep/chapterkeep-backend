package com.konkuk.chapterkeep.bookReview.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookReviewReqDto {

    private BookDto bookInfo;

    private int rating;
    private String quotation;
    private String content;
    private String coverColor;

}
