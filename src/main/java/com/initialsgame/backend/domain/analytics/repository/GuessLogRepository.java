package com.initialsgame.backend.domain.analytics.repository;

import com.initialsgame.backend.domain.analytics.entity.GuessLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuessLogRepository extends JpaRepository<GuessLog, Long> {
}
