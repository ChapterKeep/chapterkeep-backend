package com.konkuk.chapterkeep.bookReview.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NaverResultVO {

    private String lastBuildDate;
    private int total;
    private int start;
    private int display = 20;
    private List<BookVO> items;

}
