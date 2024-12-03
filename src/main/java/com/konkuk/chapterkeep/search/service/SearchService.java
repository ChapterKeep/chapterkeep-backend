package com.konkuk.chapterkeep.search.service;

import com.konkuk.chapterkeep.bookInfo.repository.BookInfoRepository;
import com.konkuk.chapterkeep.bookReview.repository.BookReviewRepository;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.common.response.exception.GeneralException;
import com.konkuk.chapterkeep.domain.BookInfo;
import com.konkuk.chapterkeep.domain.Member;
import com.konkuk.chapterkeep.member.repository.MemberRepository;
import com.konkuk.chapterkeep.search.dto.SearchBookReviewResDto;
import com.konkuk.chapterkeep.search.dto.SearchBookShelfResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final BookReviewRepository bookReviewRepository;
    private final BookInfoRepository bookInfoRepository;
    private final MemberRepository memberRepository;

    public List<SearchBookReviewResDto> getReviewsByTitle(String title) {

        if (title == null || title.trim().isEmpty()) {
            throw new GeneralException(Code.BAD_REQUEST, "유효하지 않은 검색어");
        }

        List<BookInfo> bookInfoList = bookInfoRepository.findByTitleContaining(title);

        List<SearchBookReviewResDto> result = bookInfoList.stream()
                .flatMap(bookInfo -> bookReviewRepository.findByBookInfo_BookId(bookInfo.getBookId()).stream()
                        .map(bookReview -> SearchBookReviewResDto.builder()
                                .title(bookInfo.getTitle())
                                .coverUrl(bookInfo.getCoverUrl())
                                .nickname(bookReview.getMember().getNickname())
                                .build()
                        )
                )
                .toList();

        if (result.isEmpty()) {
            throw new GeneralException(Code.NOT_FOUND, "해당 검색어에 대한 검색 결과가 존재하지 않음 : " + title);
        }

        return result;
    }

    public List<SearchBookShelfResDto> getBookShelfByNickName(String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new GeneralException(Code.INVALID_INPUT_VALUE, "유효하지 않은 검색어");
        }

        List<Member> members = memberRepository.findByNicknameContaining(nickname);

        if (members.isEmpty()) {
            throw new GeneralException(Code.NOT_FOUND, "해당 검색어에 대한 검색 결과가 존재하지 않음 : " + nickname);
        }

        return members.stream()
                .map(member -> {
                    long bookReviewCount = bookReviewRepository.countByMember_MemberId(member.getMemberId());
                    return SearchBookShelfResDto.builder()
                            .nickname(member.getNickname())
                            .profileUrl(member.getProfileUrl())
                            .bookReviewCount(bookReviewCount)
                            .build();
                })
                .toList();
    }
}
