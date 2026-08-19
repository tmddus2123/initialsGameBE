package com.initialsgame.backend.domain.word.seed;

import com.initialsgame.backend.domain.word.client.StdictApiClient;
import com.initialsgame.backend.domain.word.entity.Word;
import com.initialsgame.backend.domain.word.repository.WordRepository;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

/**
 * "words" 테이블을 표준국어대사전 API로 미리 채워두는 오프라인 배치.
 * resources/seed/candidate-words.txt 의 후보 단어 + 아직 뜻풀이가 없는 기존 단어를 대상으로
 * 사전을 조회해 존재 여부와 뜻풀이를 함께 저장한다.
 * 게임 요청 처리 경로에서는 이 API를 전혀 호출하지 않으므로, 여기서만 rate limit을 신경 쓰면 된다.
 *
 * 실행: ./gradlew bootRun --args='--spring.profiles.active=local,seed'
 */
@Slf4j
@Component
@Profile("seed")
@RequiredArgsConstructor
public class WordSeedRunner implements CommandLineRunner {

	private static final long DELAY_BETWEEN_CALLS_MS = 150;

	private final WordRepository wordRepository;
	private final StdictApiClient stdictApiClient;
	private final ResourceLoader resourceLoader;

	@Override
	public void run(String... args) throws Exception {
		List<String> candidates = readCandidates();
		List<String> missingDefinition = wordRepository.findByDefinitionsIsNull().stream()
			.map(Word::getText)
			.toList();

		Set<String> targets = new LinkedHashSet<>();
		targets.addAll(missingDefinition);
		targets.addAll(candidates);

		log.info(
			"단어 시딩을 시작합니다. 대상 {}개 (뜻풀이 미확보 기존 단어 {}개 + 후보 단어 {}개, 중복 제외).",
			targets.size(), missingDefinition.size(), candidates.size()
		);

		int added = 0;
		int updated = 0;
		int notInDictionary = 0;
		int i = 0;

		for (String word : targets) {
			i++;
			Optional<List<String>> definitions = stdictApiClient.findDefinitions(word);

			if (definitions.isEmpty()) {
				notInDictionary++;
				log.info("[{}/{}] 사전에 없음: {}", i, targets.size(), word);
				Thread.sleep(DELAY_BETWEEN_CALLS_MS);
				continue;
			}

			Word existingWord = wordRepository.findByText(word).orElse(null);
			if (existingWord == null) {
				wordRepository.save(new Word(word, definitions.get()));
				added++;
				log.info("[{}/{}] 신규 추가: {}", i, targets.size(), word);
			} else {
				existingWord.updateDefinitions(definitions.get());
				wordRepository.save(existingWord);
				updated++;
				log.info("[{}/{}] 뜻풀이 갱신: {}", i, targets.size(), word);
			}

			Thread.sleep(DELAY_BETWEEN_CALLS_MS);
		}

		log.info(
			"단어 시딩 완료. 총 {}개 처리 / 신규 추가 {}개 / 뜻풀이 갱신 {}개 / 사전에 없음 {}개.",
			targets.size(), added, updated, notInDictionary
		);
	}

	private List<String> readCandidates() throws Exception {
		Resource resource = resourceLoader.getResource("classpath:seed/candidate-words.txt");
		try (BufferedReader reader = new BufferedReader(
			new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
			return reader.lines()
				.map(String::trim)
				.filter(line -> !line.isEmpty() && !line.startsWith("#"))
				.distinct()
				.toList();
		}
	}
}
