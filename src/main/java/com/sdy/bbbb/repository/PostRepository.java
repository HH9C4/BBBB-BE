package com.sdy.bbbb.repository;

import com.sdy.bbbb.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT p FROM Post p left join fetch p.commentList WHERE p.id = ?1")
    Optional<Post> searchOneByIdWithCommentList(Long postId);

    Optional<Post> findById(Long id);

    List<Post> findPostsByAccount_IdOrderByCreatedAtDesc(Long accountId);

//    @Modifying
//    @Query(value = "DELETE from Post p where p.id > ?1" )
//    int deleteReturnAffectedRows(Long postId);
}
