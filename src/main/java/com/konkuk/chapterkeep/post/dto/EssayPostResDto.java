package com.konkuk.chapterkeep.post.dto;

import com.konkuk.chapterkeep.bookReview.dto.BookDto;
import com.konkuk.chapterkeep.bookReview.dto.BookReviewResDto;
import com.konkuk.chapterkeep.domain.BookReview;
import com.konkuk.chapterkeep.domain.Post;
import com.konkuk.chapterkeep.domain.posts.EssayContestPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EssayPostResDto {

    private Long memberId;
    private String nickname;
    private String profileUrl;
    private Long postId;
    private String title;
    private boolean isAnonymous;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private long likesCount;

    public static EssayPostResDto fromEntity(Post post, long likesCount) {

        EssayContestPost essayContestPost = (EssayContestPost) post;

        return EssayPostResDto.builder()
                .memberId(essayContestPost.getMember() != null ? essayContestPost.getMember().getMemberId() : null)
                .nickname(essayContestPost.getMember() != null ? essayContestPost.getMember().getNickname() : null)
                .profileUrl(essayContestPost.getMember() != null ? essayContestPost.getMember().getProfileUrl() : null)
                .postId(essayContestPost.getPostId())
                .title(essayContestPost.getTitle())
                .isAnonymous(essayContestPost.getIsAnonymous())
                .content(essayContestPost.getContent())
                .createdAt(essayContestPost.getCreatedDate())
                .modifiedAt(essayContestPost.getModifiedDate())
                .likesCount(likesCount)
                .build();
    }
}


