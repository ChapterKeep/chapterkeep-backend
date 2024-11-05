package com.konkuk.chapterkeep.domain.posts;

import com.konkuk.chapterkeep.domain.BookInfo;
import com.konkuk.chapterkeep.domain.Post;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@DiscriminatorValue("SINGLE")
@PrimaryKeyJoinColumn(name = "post_id")
@Getter
public class SingleLinePost extends Post {

    @Column(name = "content", nullable = false, length = 100)
    private String content;

    @OneToOne
    @JoinColumn(name = "book_id")
    private BookInfo bookInfo;
}
