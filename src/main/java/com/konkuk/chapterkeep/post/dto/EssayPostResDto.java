package com.konkuk.chapterkeep.post.dto;

import com.konkuk.chapterkeep.domain.Post;
import com.konkuk.chapterkeep.domain.posts.EssayPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EssayPostResDto {

    private Long memberId;
    private String nickname;
    private String profileUrl;
    private Long postId;
    private String postTitle;
    private boolean anonymous;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private long likesCount;

    public static EssayPostResDto fromEntity(Post post, long likesCount) {

        EssayPost essayPost = (EssayPost) post;

        return EssayPostResDto.builder()
                .memberId(essayPost.getMember() != null ? essayPost.getMember().getMemberId() : null)
                .nickname(essayPost.getMember() != null ? essayPost.getMember().getNickname() : null)
                .profileUrl(essayPost.getMember() != null ? essayPost.getMember().getProfileUrl() : null)
                .postId(essayPost.getPostId())
                .postTitle(essayPost.getTitle())
                .anonymous(essayPost.isAnonymous())
                .content(essayPost.getContent())
                .createdAt(essayPost.getCreatedDate())
                .modifiedAt(essayPost.getModifiedDate())
                .likesCount(likesCount)
                .build();
    }
}


