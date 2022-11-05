package com.sdy.bbbb.repository;

import com.sdy.bbbb.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
