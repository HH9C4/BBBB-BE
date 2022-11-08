package com.sdy.bbbb.repository;

import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.Like;
import com.sdy.bbbb.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndAccount(Post post, Account account);

    boolean existsByPostAndAccount(Post post, Account account);

    List<Like> findByAccount_Id(Long account_id);
}
