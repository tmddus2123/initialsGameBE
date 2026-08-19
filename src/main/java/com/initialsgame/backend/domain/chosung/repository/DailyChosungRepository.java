package com.initialsgame.backend.domain.chosung.repository;

import com.initialsgame.backend.domain.chosung.entity.DailyChosung;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyChosungRepository extends JpaRepository<DailyChosung, Long> {

	Optional<DailyChosung> findByDate(LocalDate date);
}
