package com.konkuk.chapterkeep.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("ESSAY")
@PrimaryKeyJoinColumn(name = "post_id")
@Getter
@Setter
public class EssayContestPost extends Post {

    @Column(name = "content", nullable = false)
    private String content;
}
