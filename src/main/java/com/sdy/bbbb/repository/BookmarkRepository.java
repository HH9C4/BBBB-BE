package com.sdy.bbbb.repository;

import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.Bookmark;
import com.sdy.bbbb.entity.Gu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface
BookmarkRepository extends JpaRepository<Bookmark, Long> {
    boolean existsByGuAndAccount(Gu gu, Account account);

    Optional<Bookmark> findByGuAndAccount(Gu gu, Account account);

    List<Bookmark> findBookmarkByAccount_IdOrderByBookmarked(Long account_id);
}
