package com.konkuk.chapterkeep.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("SINGLE")
@PrimaryKeyJoinColumn(name = "post_id")
@Getter
@Setter
public class SingleLinePost extends Post {

    private String content;
}
