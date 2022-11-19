package com.sdy.bbbb.repository;

import com.sdy.bbbb.entity.Gu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GuRepository extends JpaRepository<Gu, Long> {
    Optional<Gu> findGuByGuName(String gu);

}
