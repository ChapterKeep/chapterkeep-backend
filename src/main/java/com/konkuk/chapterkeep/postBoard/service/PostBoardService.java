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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostBoardService {

    private final BookInfoRepository bookInfoRepository;
    private final BookReviewRepository bookReviewRepository;
    private final KonkukBookListRepository konkukBookListRepository;
    private final EssayPostRepository essayPostRepository;
    private final LikesRepository likesRepository;


    public List<BookReviewRecommendResDto> getMostReviewedBook() {
        try {

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
        }catch (GeneralException e){
            throw e;
        }catch (Exception e){
            throw new GeneralException(Code.INTERNAL_ERROR, "독서 기록 수 상위 도서 조회 도중 알 수 없는 오류 발생");
        }
    }

    public PostBoardResDto getPostBoardData() {
        List<RentalCountRecommendResDto> mostRentedBookList;
        List<MostLikedEssayPostResDto> mostLikedEssayPostList;

        try {
            // 대출 수 상위 도서
            List<KonkukBookList> mostRentedBooks = konkukBookListRepository.findTop3ByOrderByRentalCountDesc();
            mostRentedBookList = mostRentedBooks.stream()
                    .map(konkukBookList -> RentalCountRecommendResDto.builder()
                            .title(konkukBookList.getTitle())
                            .library_url(konkukBookList.getLibraryUrl())
                            .build()
                    )
                    .collect(Collectors.toList());
        }catch (Exception e){
            throw new GeneralException(Code.INTERNAL_ERROR, "대출 수 상위 도서 조회 도중 알 수 없는 오류 발생");
        }

        try {
            // 좋아요 수 상위 도서
            List<Long> mostLikedEssayPostIds = likesRepository.findTop3PostIdsByLikesCount();
            List<EssayPost> mostLikedEssayPosts = essayPostRepository.findPostsByPostIds(mostLikedEssayPostIds);
            mostLikedEssayPostList = mostLikedEssayPosts.stream()
                    .map(post -> MostLikedEssayPostResDto.builder()
                            .profileUrl(post.getMember().getProfileUrl())
                            .nickname(post.getMember().getNickname())
                            .postTitle(post.getTitle())
                            .anonymous(post.isAnonymous())
                            .likesCount(likesRepository.countByPost_PostId(post.getPostId()))
                            .build()
                    )
                    .collect(Collectors.toList());
        }catch (Exception e){
            throw new GeneralException(Code.INTERNAL_ERROR, "좋아요 수 상위 도서 조회 도중 알 수 없는 오류 발생");
        }

        return PostBoardResDto.builder()
                .rentalCountRecommendResDtoList(mostRentedBookList)
                .essayPostResDtoList(mostLikedEssayPostList)
                .build();
    }
}
