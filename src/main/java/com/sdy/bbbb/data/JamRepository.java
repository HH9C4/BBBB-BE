package com.sdy.bbbb.data;

import com.sdy.bbbb.data.entity.JamOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JamRepository extends JpaRepository<JamOfWeek, Long> {

}
