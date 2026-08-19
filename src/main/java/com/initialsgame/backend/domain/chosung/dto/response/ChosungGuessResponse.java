package com.initialsgame.backend.domain.chosung.dto.response;

import java.util.List;

public record ChosungGuessResponse(
	boolean correct,
	String message,
	List<String> definitions
) {

	public static ChosungGuessResponse ofCorrect(List<String> definitions) {
		return new ChosungGuessResponse(true, "정답입니다!", definitions);
	}

	public static ChosungGuessResponse ofIncorrect(String message) {
		return new ChosungGuessResponse(false, message, List.of());
	}
}
