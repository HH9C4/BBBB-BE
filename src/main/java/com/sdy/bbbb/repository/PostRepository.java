package com.sdy.bbbb.repository;

import com.sdy.bbbb.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findPostsByGuOrderByCreatedAtDesc(String gu);
    List<Post> findPostsByGuOrderByLikeCountDescCreatedAtDesc(String gu);
    Optional<Post> findById(Long id);


    List<Post> findPostsByAccount_Id(Long accountId);

    List<Long> findPostIdByAccount_Id(Long accountId);
}
