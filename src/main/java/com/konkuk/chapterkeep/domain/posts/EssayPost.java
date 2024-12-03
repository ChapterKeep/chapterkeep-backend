package com.konkuk.chapterkeep.domain.posts;

import com.konkuk.chapterkeep.domain.Member;
import com.konkuk.chapterkeep.domain.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("ESSAY")
@PrimaryKeyJoinColumn(name = "post_id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EssayPost extends Post {

    @Column(name = "content", nullable = false, length = 2000)
    private String content;

    @Builder
    protected EssayPost(Member member, String title, boolean isAnonymous, String content) {
        super(member, title, isAnonymous);
        this.content = content;
    }

    // 생성 메서드
    public static EssayPost createEssayPost(Member member, String title, boolean isAnonymous, String content) {
        return EssayPost.builder()
                .member(member)
                .title(title)
                .isAnonymous(isAnonymous)
                .content(content)
                .build();
    }

    @Override
    public void update(String title, boolean isAnonymous) {
        super.update(title, isAnonymous);
    }

    public void update(String title, Boolean isAnonymous, String content) {
        this.update(title, isAnonymous);
        this.content = content;
    }

}
