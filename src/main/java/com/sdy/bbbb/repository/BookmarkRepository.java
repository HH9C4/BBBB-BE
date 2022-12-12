package com.sdy.bbbb.repository;

import com.sdy.bbbb.entity.Account;
import com.sdy.bbbb.entity.Bookmark;
import com.sdy.bbbb.entity.Gu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface
BookmarkRepository extends JpaRepository<Bookmark, Long> {
    boolean existsByGuAndAccount(Gu gu, Account account);

    boolean existsByGu_GuNameAndAccount(String guName, Account account);

    Optional<Bookmark> findByGuAndAccount(Gu gu, Account account);

    List<Bookmark> findBookmarkByAccount_IdOrderByBookmarked(Long account_id);


    @Query(value = "select b from Bookmark b join fetch b.gu where b.account.id = ?1")
    List<Bookmark> findBookmarksByAccountId(Long id);

    @Query(value = "select b from Bookmark b join fetch b.account where b.gu.guName = ?1")
    List<Bookmark> findByGuFetchAccount(String guName);
}
