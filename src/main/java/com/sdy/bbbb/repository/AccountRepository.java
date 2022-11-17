package com.sdy.bbbb.repository;

import com.sdy.bbbb.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByKakaoId(Long kakaoId);
    Optional<Account> findByEmail(String email);
    Optional<Account> findByNaverId(String naverId);
}
