package com.petcommunity.repository;

import com.petcommunity.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByOrderByCreateTimeDesc(Pageable pageable);
    List<Post> findByUserIdOrderByCreateTimeDesc(Long userId);

    @Query("SELECT p FROM Post p WHERE p.topic IS NOT NULL AND p.topic != '' ORDER BY p.createTime DESC")
    Page<Post> findTopicPosts(Pageable pageable);
}
