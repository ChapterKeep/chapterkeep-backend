package com.konkuk.chapterkeep.domain.posts;

import com.konkuk.chapterkeep.domain.Member;
import com.konkuk.chapterkeep.domain.Post;
import com.konkuk.chapterkeep.domain.enums.CoverColor;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("ESSAY")
@PrimaryKeyJoinColumn(name = "post_id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EssayContestPost extends Post {

    @Column(name = "content", nullable = false, length = 2000)
    private String content;

    @Builder
    protected EssayContestPost(Member member, String title, boolean isAnonymous, String content) {
        super(member, title, isAnonymous);
        this.content = content;
    }

    // 생성 메서드
    public static EssayContestPost createEssayPost(Member member, String title, boolean isAnonymous, String content) {
        return EssayContestPost.builder()
                .member(member)
                .title(title)
                .isAnonymous(isAnonymous)
                .content(content)
                .build();
    }

    @Override
    public void update(String title, Boolean isAnonymous) {
        super.update(title, isAnonymous);
    }

    public void update(String title, Boolean isAnonymous, String content) {
        this.update(title, isAnonymous);
        this.content = content;
    }

}
