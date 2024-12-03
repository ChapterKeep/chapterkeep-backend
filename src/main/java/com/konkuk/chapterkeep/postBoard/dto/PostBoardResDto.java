package com.konkuk.chapterkeep.postBoard.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostBoardResDto {

//    private List<BookReviewRecommendResDto> bookReviewRecommendResDtoList;
    private List<RentalCountRecommendResDto> rentalCountRecommendResDtoList;
    private List<MostLikedEssayPostResDto> essayPostResDtoList;

}