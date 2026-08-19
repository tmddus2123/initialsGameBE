package com.initialsgame.backend.domain.chosung.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record ChosungGuessRequest(
	@NotBlank(message = "정답 단어를 입력해주세요.")
	String word,
	List<String> submittedWords
) {

	public ChosungGuessRequest {
		if (submittedWords == null) {
			submittedWords = List.of();
		}
	}
}
