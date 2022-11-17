package com.sdy.bbbb.repository;

import com.sdy.bbbb.entity.Post;
import com.sdy.bbbb.querydsl.PostRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

//QuerydslPredicateExecutor<Post>
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    @Query(value = "select * from post p where p.content like %?2% order by p.created_at", nativeQuery = true)
    List<Post> test(Integer type, String searchWord, String sort);

    @Query(value = "SELECT p FROM Post p left join fetch p.commentList WHERE p.id = ?1")
    Optional<Post> searchOneByIdWithNativeQuery(Long postId);

//    @Query("select p from Post p join fetch p.imageList")
    Set<Post> findPostsByGuNameOrderByCreatedAtDesc(String gu);

    List<Post> findPostsByGuNameOrderByLikeCountDescCreatedAtDesc(String gu);

    List<Post> findPostsByContentContainsOrderByCreatedAtDesc(String searchWord);

    List<Post> findPostsByContentContainsOrderByLikeCountDescCreatedAtDesc(String searchWord);

    Optional<Post> findById(Long id);

    List<Post> findPostsByAccount_IdOrderByCreatedAtDesc(Long accountId);

}
