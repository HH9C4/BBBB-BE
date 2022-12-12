package com.sdy.bbbb.repository;

import com.sdy.bbbb.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

    boolean existsByLevelAndReporterIdAndReportedId(Long level, Long reporterId, Long reportedId);

    void deleteAllByLevelAndReportedId(Long level, Long reportedId);
}
