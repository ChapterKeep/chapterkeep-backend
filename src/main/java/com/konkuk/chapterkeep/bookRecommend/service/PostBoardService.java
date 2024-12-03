package com.konkuk.chapterkeep.bookRecommend.service;

import com.konkuk.chapterkeep.bookInfo.repository.BookInfoRepository;
import com.konkuk.chapterkeep.bookRecommend.dto.BookReviewRecommendResDto;
import com.konkuk.chapterkeep.bookRecommend.dto.MostLikedEssayPostResDto;
import com.konkuk.chapterkeep.bookRecommend.dto.PostBoardResDto;
import com.konkuk.chapterkeep.bookRecommend.dto.RentalCountRecommendResDto;
import com.konkuk.chapterkeep.bookRecommend.repository.KonkukBookListRepository;
import com.konkuk.chapterkeep.bookReview.repository.BookReviewRepository;
import com.konkuk.chapterkeep.domain.KonkukBookList;
import com.konkuk.chapterkeep.domain.Likes;
import com.konkuk.chapterkeep.domain.Post;
import com.konkuk.chapterkeep.domain.posts.EssayPost;
import com.konkuk.chapterkeep.likes.repository.LikesRepository;
import com.konkuk.chapterkeep.post.dto.EssayPostResDto;
import com.konkuk.chapterkeep.post.repository.EssayPostRepository;
import com.konkuk.chapterkeep.post.service.EssayPostService;
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
    private final EssayPostService essayPostService;
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

        List<KonkukBookList> mostRentedBooks = konkukBookListRepository.findTop3ByOrderByRentalCountDesc();
        List<RentalCountRecommendResDto> mostRentedBookList = mostRentedBooks.stream()
                .map(konkukBookList -> RentalCountRecommendResDto.builder()
                        .title(konkukBookList.getTitle())
                        .library_url(konkukBookList.getLibraryUrl())
                        .build()
                )
                .collect(Collectors.toList());

        List<Long> mostLikedEssayPostIds = likesRepository.findTop3PostIdsByLikesCount();
        List<EssayPost> mostLikedEssayPosts = essayPostRepository.findPostsByPostIds(mostLikedEssayPostIds);
        List<MostLikedEssayPostResDto> mostLikedEssayPostList = mostLikedEssayPosts.stream()
                .map(post -> MostLikedEssayPostResDto.builder()
                        .profileUrl(post.getMember().getProfileUrl())
                        .nickname(post.getMember().getNickname())
                        .title(post.getTitle())
                        .anonymous(post.isAnonymous())
                        .likesCount(likesRepository.countByPost_PostId(post.getPostId()))
                        .build()
                )
                .collect(Collectors.toList());

        return PostBoardResDto.builder()
                .rentalCountRecommendResDtoList(mostRentedBookList)
                .essayPostResDtoList(mostLikedEssayPostList)
                .build();

    }
}
