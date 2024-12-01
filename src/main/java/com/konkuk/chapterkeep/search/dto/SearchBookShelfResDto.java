package com.konkuk.chapterkeep.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchBookShelfResDto {

    private List<ShelfInfoDto> shelfInfoList;

}
