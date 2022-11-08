package com.sdy.bbbb.repository;

import com.sdy.bbbb.entity.MyPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyPageRepository extends JpaRepository<MyPage, Long> {


}
