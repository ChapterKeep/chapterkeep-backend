package com.konkuk.chapterkeep.post.repository;

import com.konkuk.chapterkeep.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByMember_MemberId(Long memberId);
}
