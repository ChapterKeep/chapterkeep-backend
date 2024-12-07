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
public class EssayPostCreateResDto {

    private Long memberId;
    private Long postId;
    private LocalDateTime createdAt;

}


