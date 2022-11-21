package com.sdy.bbbb.data;

import com.sdy.bbbb.data.entity.JamOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JamRepository extends JpaRepository<JamOfWeek, Long> {

    List<JamOfWeek> findByIsWeekend(boolean isWeekend);
}
