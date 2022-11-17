package com.sdy.bbbb.repository;

import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.Comment;
import com.sdy.bbbb.entity.Like;
import com.sdy.bbbb.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    // 게시글, 유저 정보
    Optional<Like> findByPostAndAccount(Post post, Account account);
    // 게시글, 유저 정보
    boolean existsByPostAndAccount(Post post, Account account);
    // 유저 정보
    List<Like> findLikesByAccount_idAndLikeLevelOrderByIdDesc(Long account_id, Integer likeLevel);
    // 댓글, 유저 정보
    Optional<Like> findByCommentAndAccount(Comment comment, Account account);
    // 댓글, 유저 정보
    boolean existsByCommentAndAccount(Comment comment, Account account);

}
