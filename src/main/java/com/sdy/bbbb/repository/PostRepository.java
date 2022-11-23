package com.sdy.bbbb.repository;

import com.sdy.bbbb.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

//QuerydslPredicateExecutor<Post>
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "select * from post p where p.content like %?2% order by p.created_at", nativeQuery = true)
    List<Post> test(Integer type, String searchWord, String sort);

    @Query(value = "SELECT p FROM Post p left join fetch p.commentList WHERE p.id = ?1")
    Optional<Post> searchOneByIdWithCommentList(Long postId);

//    @Query("select p from Post p join fetch p.imageList") set사용할거면 linked hash set, 그게아니면 distinct
    Set<Post> findPostsByGuNameOrderByCreatedAtDesc(String gu);

    List<Post> findPostsByGuNameOrderByLikeCountDescCreatedAtDesc(String gu);

    List<Post> findPostsByContentContainsOrderByCreatedAtDesc(String searchWord);

    List<Post> findPostsByContentContainsOrderByLikeCountDescCreatedAtDesc(String searchWord);

    Optional<Post> findById(Long id);

    List<Post> findPostsByAccount_IdOrderByCreatedAtDesc(Long accountId);

    List<Post> findPostsByAccount_IdOrderByCreatedAtAsc(Long accountId);

}
