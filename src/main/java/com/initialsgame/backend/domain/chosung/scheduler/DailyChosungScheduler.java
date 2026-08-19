package com.initialsgame.backend.domain.chosung.scheduler;

import com.initialsgame.backend.domain.chosung.service.ChosungService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DailyChosungScheduler {

	private final ChosungService chosungService;

	@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
	public void generateTodayPuzzle() {
		chosungService.ensurePuzzleForDate(LocalDate.now());
	}
}
