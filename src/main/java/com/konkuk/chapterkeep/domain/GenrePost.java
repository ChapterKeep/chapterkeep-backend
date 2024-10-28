package com.konkuk.chapterkeep.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("GENRE")
@PrimaryKeyJoinColumn(name = "post_id")
@Getter
@Setter
public class GenrePost extends Post {

    @Enumerated(EnumType.STRING)
    @Column(name = "genre", nullable = false)
    private Genre genre;

    @Column(name = "content", nullable = false)
    private String content;

}
