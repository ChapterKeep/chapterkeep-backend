package com.konkuk.chapterkeep.domain.posts;

import com.konkuk.chapterkeep.domain.Post;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("FREE")
@PrimaryKeyJoinColumn(name = "post_id")
@Getter
@Setter
public class FreePost extends Post {

    @Column(name = "content", nullable = false)
    private String content;

}
