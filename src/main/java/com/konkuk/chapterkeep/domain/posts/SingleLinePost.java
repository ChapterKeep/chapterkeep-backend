package com.konkuk.chapterkeep.domain.posts;

import com.konkuk.chapterkeep.domain.BookInfo;
import com.konkuk.chapterkeep.domain.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("SINGLE")
@PrimaryKeyJoinColumn(name = "post_id")
@Getter
@Setter
public class SingleLinePost extends Post {

    private String content;

    @OneToOne
    @JoinColumn(name = "book_id")
    private BookInfo bookInfo;
}
