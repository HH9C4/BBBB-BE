package com.sdy.bbbb.repository;

import com.sdy.bbbb.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p join fetch p.imageList")
    List<Post> findPostsByGuOrderByCreatedAtDesc(String gu);

    List<Post> findPostsByGuOrderByLikeCountDescCreatedAtDesc(String gu);

    List<Post> findPostsByTagContainsAndContentContainsOrderByCreatedAtDesc(String searchWord, String searchWord1);

    List<Post> findPostsByTagContainsAndContentContainsOrderByLikeCountDescCreatedAtDesc(String searchWord, String searchWord1);
    Optional<Post> findById(Long id);


    List<Post> findPostsByAccount_Id(Long accountId);

    List<Long> findPostIdByAccount_Id(Long accountId);


}
