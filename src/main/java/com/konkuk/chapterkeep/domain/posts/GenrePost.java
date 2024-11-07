package com.konkuk.chapterkeep.domain.posts;

import com.konkuk.chapterkeep.domain.enums.Genre;
import com.konkuk.chapterkeep.domain.Post;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@DiscriminatorValue("GENRE")
@PrimaryKeyJoinColumn(name = "post_id")
@Getter
public class GenrePost extends Post {

    @Enumerated(EnumType.STRING)
    @Column(name = "genre", nullable = false)
    private Genre genre;

    @Column(name = "content", nullable = false, length = 500)
    private String content;

}
