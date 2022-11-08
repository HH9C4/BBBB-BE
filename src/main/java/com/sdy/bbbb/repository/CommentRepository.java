package com.sdy.bbbb.repository;

import com.sdy.bbbb.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByAccountId(Long accountId);

    List<Comment> findByPostId(Long postId);

    Optional<Comment> findById(Long id);
}
