package com.initialsgame.backend.domain.analytics.repository;

import com.initialsgame.backend.domain.analytics.entity.PageViewLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageViewLogRepository extends JpaRepository<PageViewLog, Long> {
}
