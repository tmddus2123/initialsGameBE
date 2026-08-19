package com.initialsgame.backend.domain.chosung.dto.response;

import com.initialsgame.backend.domain.chosung.entity.DailyChosung;
import java.time.LocalDate;
import java.util.List;

public record TodayChosungResponse(
	LocalDate date,
	String initials,
	int syllableCount,
	List<DiscoveredWord> discoveredWords
) {

	/**
	 * discoveredWords: 지금까지 이 초성 조합으로 확인된(다른 사용자 포함) 누적 정답 단어 목록.
	 * 사전 전체를 커버하지 못하므로 "전체 정답"이 아니라 "지금까지 발견된 것"으로 해석해야 한다.
	 */
	public static TodayChosungResponse from(DailyChosung dailyChosung, List<DiscoveredWord> discoveredWords) {
		return new TodayChosungResponse(
			dailyChosung.getDate(),
			dailyChosung.getInitials(),
			dailyChosung.getSyllableCount(),
			discoveredWords
		);
	}

	public record DiscoveredWord(String word, List<String> definitions) {
	}
}
