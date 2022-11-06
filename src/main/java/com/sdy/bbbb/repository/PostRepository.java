package com.sdy.bbbb.repository;

import com.sdy.bbbb.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findPostsByGuOrderByCreatedAtDesc(String gu);

    List<Post> findPostsByGuOrderByLikeCountDesc(String gu);

    List<Post> findPostsByGuAndCategoryOrderByCreatedAt(String gu, String category);
    Optional<Post> findById(Long id);
}
