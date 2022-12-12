package com.sdy.bbbb.repository.data;

import com.sdy.bbbb.entity.data.JamOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JamRepository extends JpaRepository<JamOfWeek, Long> {

    List<JamOfWeek> findByIsWeekend(boolean isWeekend);
}
