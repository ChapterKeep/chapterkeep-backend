package com.konkuk.chapterkeep.domain.posts;

import com.konkuk.chapterkeep.domain.Post;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@DiscriminatorValue("ESSAY")
@PrimaryKeyJoinColumn(name = "post_id")
@Getter
public class EssayContestPost extends Post {

    @Column(name = "content", nullable = false, length = 2000)
    private String content;
}
