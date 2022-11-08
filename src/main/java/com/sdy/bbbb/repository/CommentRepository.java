package com.sdy.bbbb.repository;

import com.sdy.bbbb.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByAccount_Id(Long accountId);
}
