package com.sdy.bbbb.repository;

import com.sdy.bbbb.entity.Post;
import com.sdy.bbbb.querydsl.PostRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

//    @Query("select p from Post p join fetch p.imageList")
    Set<Post> findPostsByGuNameOrderByCreatedAtDesc(String gu);

    List<Post> findPostsByGuNameOrderByLikeCountDescCreatedAtDesc(String gu);

    List<Post> findPostsByContentContainsOrderByCreatedAtDesc(String searchWord);

    List<Post> findPostsByContentContainsOrderByLikeCountDescCreatedAtDesc(String searchWord);

    Optional<Post> findById(Long id);

    List<Post> findPostsByAccount_IdOrderByCreatedAtDesc(Long accountId);

}
