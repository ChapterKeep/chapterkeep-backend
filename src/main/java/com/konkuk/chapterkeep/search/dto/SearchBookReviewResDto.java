package com.konkuk.chapterkeep.search.dto;

import com.konkuk.chapterkeep.bookReview.dto.BookReviewResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchBookReviewResDto {

    private String title;
    private String coverUrl;
    private String nickname;

}
