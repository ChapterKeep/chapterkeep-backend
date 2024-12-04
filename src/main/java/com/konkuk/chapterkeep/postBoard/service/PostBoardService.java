package com.konkuk.chapterkeep.postBoard.service;

import com.konkuk.chapterkeep.bookInfo.repository.BookInfoRepository;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.common.response.exception.GeneralException;
import com.konkuk.chapterkeep.postBoard.dto.BookReviewRecommendResDto;
import com.konkuk.chapterkeep.postBoard.dto.MostLikedEssayPostResDto;
import com.konkuk.chapterkeep.postBoard.dto.PostBoardResDto;
import com.konkuk.chapterkeep.postBoard.dto.RentalCountRecommendResDto;
import com.konkuk.chapterkeep.postBoard.repository.KonkukBookListRepository;
import com.konkuk.chapterkeep.bookReview.repository.BookReviewRepository;
import com.konkuk.chapterkeep.domain.KonkukBookList;
import com.konkuk.chapterkeep.domain.posts.EssayPost;
import com.konkuk.chapterkeep.likes.repository.LikesRepository;
import com.konkuk.chapterkeep.post.repository.EssayPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostBoardService {

    private final BookInfoRepository bookInfoRepository;
    private final BookReviewRepository bookReviewRepository;
    private final KonkukBookListRepository konkukBookListRepository;
    private final EssayPostRepository essayPostRepository;
    private final LikesRepository likesRepository;


    public List<BookReviewRecommendResDto> getMostReviewedBook() {

        Pageable pageable = PageRequest.of(0, 3);
        List<Long> mostReviewedBookIds = bookReviewRepository.findBookIdsOrderByReviewCountDesc(pageable);

        return mostReviewedBookIds.stream()
                .map(bookId -> bookInfoRepository.findById(bookId)
                        .map(bookInfo -> BookReviewRecommendResDto.builder()
                                .title(bookInfo.getTitle())
                                .writer(bookInfo.getWriter())
                                .cover_url(bookInfo.getCoverUrl())
                                .build()
                        )
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public PostBoardResDto getPostBoardData() {
        try {

            List<KonkukBookList> mostRentedBooks = konkukBookListRepository.findTop3ByOrderByRentalCountDesc();
            if (mostRentedBooks.isEmpty()) {
                throw new GeneralException(Code.NOT_FOUND, "대출 수 상위 도서를 찾을 수 없습니다.");
            }
            List<RentalCountRecommendResDto> mostRentedBookList = mostRentedBooks.stream()
                    .map(konkukBookList -> RentalCountRecommendResDto.builder()
                            .title(konkukBookList.getTitle())
                            .library_url(konkukBookList.getLibraryUrl())
                            .build()
                    )
                    .collect(Collectors.toList());

            List<Long> mostLikedEssayPostIds = likesRepository.findTop3PostIdsByLikesCount();
            if (mostLikedEssayPostIds.isEmpty()) {
                throw new GeneralException(Code.NOT_FOUND, "좋아요 수 상위 게시글을 찾을 수 없음");
            }
            List<EssayPost> mostLikedEssayPosts = essayPostRepository.findPostsByPostIds(mostLikedEssayPostIds);
            if (mostLikedEssayPosts.isEmpty()) {
                throw new GeneralException(Code.NOT_FOUND, "좋아요 수 상위 게시글 데이터를 찾을 수 없음");
            }
            List<MostLikedEssayPostResDto> mostLikedEssayPostList = mostLikedEssayPosts.stream()
                    .map(post -> MostLikedEssayPostResDto.builder()
                            .profileUrl(post.getMember().getProfileUrl())
                            .nickname(post.getMember().getNickname())
                            .postTitle(post.getTitle())
                            .anonymous(post.isAnonymous())
                            .likesCount(likesRepository.countByPost_PostId(post.getPostId()))
                            .build()
                    )
                    .collect(Collectors.toList());

            return PostBoardResDto.builder()
                    .rentalCountRecommendResDtoList(mostRentedBookList)
                    .essayPostResDtoList(mostLikedEssayPostList)
                    .build();
        } catch (Exception e) {
            throw new GeneralException(Code.INTERNAL_ERROR, "게시판 데이터를 불러오는 중 오류 발생: " + e.getMessage());
        }
    }
}
