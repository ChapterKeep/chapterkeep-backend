package com.konkuk.chapterkeep.post.repository;

import com.konkuk.chapterkeep.domain.posts.EssayPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EssayPostRepository extends JpaRepository<EssayPost, Long> {
    List<EssayPost> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword);

    @Query("SELECT e FROM EssayPost e WHERE e.postId IN :postIds")
    List<EssayPost> findPostsByPostIds(@Param("postIds") List<Long> postIds);
}
