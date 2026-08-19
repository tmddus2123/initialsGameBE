package com.initialsgame.backend.domain.chosung.controller;

import com.initialsgame.backend.domain.chosung.dto.request.ChosungGuessRequest;
import com.initialsgame.backend.domain.chosung.dto.response.ChosungGuessResponse;
import com.initialsgame.backend.domain.chosung.dto.response.TodayChosungResponse;
import com.initialsgame.backend.domain.chosung.service.ChosungService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Chosung", description = "초성게임 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chosung")
public class ChosungController {

	private final ChosungService chosungService;

	@Operation(summary = "오늘의 초성 문제 조회")
	@GetMapping("/today")
	public TodayChosungResponse getTodayPuzzle() {
		return chosungService.getTodayPuzzle();
	}

	@Operation(summary = "오늘의 초성 정답 제출")
	@PostMapping("/guess")
	public ChosungGuessResponse guess(@Valid @RequestBody ChosungGuessRequest request) {
		return chosungService.guess(request);
	}
}
