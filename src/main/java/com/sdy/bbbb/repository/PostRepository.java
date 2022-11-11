package com.sdy.bbbb.repository;

import com.sdy.bbbb.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {

//    @Query("select p from Post p join fetch p.imageList where p.guName = #{gu}")
    Set<Post> findPostsByGuNameOrderByCreatedAtDesc(String gu);

    List<Post> findPostsByGuNameOrderByLikeCountDescCreatedAtDesc(String gu);

    List<Post> findPostsByTagContainsAndContentContainsOrderByCreatedAtDesc(String searchWord, String searchWord1);

    List<Post> findPostsByTagContainsAndContentContainsOrderByLikeCountDescCreatedAtDesc(String searchWord, String searchWord1);

    Optional<Post> findById(Long id);

    List<Post> findPostsByAccount_Id(Long accountId);

}
