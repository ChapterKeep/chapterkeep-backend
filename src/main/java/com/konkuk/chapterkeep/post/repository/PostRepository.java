package com.konkuk.chapterkeep.post.repository;

import com.konkuk.chapterkeep.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {


}
