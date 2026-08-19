package com.initialsgame.backend.domain.chosung.service;

import com.initialsgame.backend.common.exception.ErrorCode;
import com.initialsgame.backend.common.exception.InitialsGameException;
import com.initialsgame.backend.common.util.HangulUtils;
import com.initialsgame.backend.domain.analytics.entity.GuessLog;
import com.initialsgame.backend.domain.analytics.entity.PageViewLog;
import com.initialsgame.backend.domain.analytics.repository.GuessLogRepository;
import com.initialsgame.backend.domain.analytics.repository.PageViewLogRepository;
import com.initialsgame.backend.domain.chosung.dto.request.ChosungGuessRequest;
import com.initialsgame.backend.domain.chosung.dto.response.ChosungGuessResponse;
import com.initialsgame.backend.domain.chosung.dto.response.TodayChosungResponse;
import com.initialsgame.backend.domain.chosung.entity.DailyChosung;
import com.initialsgame.backend.domain.chosung.repository.DailyChosungRepository;
import com.initialsgame.backend.domain.word.client.StdictApiClient;
import com.initialsgame.backend.domain.word.entity.Word;
import com.initialsgame.backend.domain.word.repository.WordRepository;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChosungService {

	private static final List<Integer> PUZZLE_SYLLABLE_COUNTS = List.of(2, 3);

	private final DailyChosungRepository dailyChosungRepository;
	private final WordRepository wordRepository;
	private final StdictApiClient stdictApiClient;
	private final GuessLogRepository guessLogRepository;
	private final PageViewLogRepository pageViewLogRepository;

	@Transactional
	public TodayChosungResponse getTodayPuzzle() {
		DailyChosung dailyChosung = ensurePuzzleForDate(LocalDate.now());
		pageViewLogRepository.save(new PageViewLog(dailyChosung.getDate()));
		// DB 콜레이션 설정에 기대지 않고 Java의 문자열 자연 순서로 직접 정렬한다.
		// 완성형 한글 음절(가~힣)은 유니코드 코드포인트 순서가 가나다순과 일치한다.
		List<TodayChosungResponse.DiscoveredWord> discoveredWords = wordRepository
			.findByInitialsAndSyllableCount(dailyChosung.getInitials(), dailyChosung.getSyllableCount())
			.stream()
			.sorted(Comparator.comparing(Word::getText))
			.map(word -> new TodayChosungResponse.DiscoveredWord(word.getText(), word.getDefinitions()))
			.toList();
		return TodayChosungResponse.from(dailyChosung, discoveredWords);
	}

	@Transactional
	public ChosungGuessResponse guess(ChosungGuessRequest request) {
		DailyChosung dailyChosung = ensurePuzzleForDate(LocalDate.now());
		String guessedWord = request.word().trim();

		if (request.submittedWords().contains(guessedWord)) {
			return ChosungGuessResponse.ofIncorrect("이미 제출한 단어예요.");
		}

		String guessedInitials;
		try {
			guessedInitials = HangulUtils.extractInitials(guessedWord);
		} catch (IllegalArgumentException e) {
			return ChosungGuessResponse.ofIncorrect("한글 단어만 입력할 수 있어요.");
		}

		if (!guessedInitials.equals(dailyChosung.getInitials())) {
			return ChosungGuessResponse.ofIncorrect("오늘의 초성과 일치하지 않아요.");
		}

		Word word = wordRepository.findByText(guessedWord).orElse(null);
		if (word != null) {
			guessLogRepository.save(new GuessLog(guessedWord, true, dailyChosung.getDate()));
			return ChosungGuessResponse.ofCorrect(word.getDefinitions());
		}

		// DB 캐시에 없는 단어는 표준국어대사전 API로 확인하고, 존재하면 다음부터는 API 호출 없이 쓰도록 캐시에 저장한다.
		Optional<List<String>> definitions = stdictApiClient.findDefinitions(guessedWord);
		if (definitions.isEmpty()) {
			guessLogRepository.save(new GuessLog(guessedWord, false, dailyChosung.getDate()));
			return ChosungGuessResponse.ofIncorrect("사전에 등록되지 않은 단어예요.");
		}

		wordRepository.save(new Word(guessedWord, definitions.get()));
		guessLogRepository.save(new GuessLog(guessedWord, true, dailyChosung.getDate()));
		return ChosungGuessResponse.ofCorrect(definitions.get());
	}

	@Transactional
	public DailyChosung ensurePuzzleForDate(LocalDate date) {
		return dailyChosungRepository.findByDate(date)
			.orElseGet(() -> generatePuzzleForDate(date));
	}

	private DailyChosung generatePuzzleForDate(LocalDate date) {
		Word word = wordRepository.findRandomBySyllableCountIn(PUZZLE_SYLLABLE_COUNTS)
			.orElseThrow(() -> new InitialsGameException(ErrorCode.NO_WORDS_AVAILABLE));
		DailyChosung dailyChosung = new DailyChosung(date, word.getInitials(), word.getSyllableCount());
		return dailyChosungRepository.save(dailyChosung);
	}
}
