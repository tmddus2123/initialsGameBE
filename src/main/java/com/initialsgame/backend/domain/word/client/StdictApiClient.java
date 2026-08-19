package com.initialsgame.backend.domain.word.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.initialsgame.backend.domain.word.client.dto.StdictSearchResponse;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * 국립국어원 표준국어대사전 Open API(https://stdict.korean.go.kr) 연동 클라이언트.
 * API 키는 opendict.korean.go.kr 에서 발급받아 DICTIONARY_API_KEY 환경변수로 주입한다.
 * 이 API는 응답 Content-Type을 "text/json"으로 내려줘 Spring 기본 Jackson 컨버터가
 * application/json이 아니라며 거부하므로, 본문을 String으로 받아 직접 역직렬화한다.
 */
@Slf4j
@Component
public class StdictApiClient {

	private final RestClient restClient;
	private final ObjectMapper objectMapper;
	private final String apiKey;

	public StdictApiClient(
		RestClient.Builder restClientBuilder,
		ObjectMapper objectMapper,
		@Value("${dictionary.api.base-url}") String baseUrl,
		@Value("${dictionary.api.key:}") String apiKey
	) {
		this.restClient = restClientBuilder.baseUrl(baseUrl).build();
		this.objectMapper = objectMapper;
		this.apiKey = apiKey;
	}

	/**
	 * 단어가 사전에 존재하면 동음이의어를 포함한 모든 뜻풀이를, 존재하지 않으면 빈 값을 반환한다.
	 * 존재 여부 확인과 뜻풀이 조회를 API 호출 한 번으로 함께 처리한다.
	 */
	public Optional<List<String>> findDefinitions(String word) {
		if (apiKey == null || apiKey.isBlank()) {
			log.warn("dictionary.api.key가 설정되지 않아 표준국어대사전 조회를 건너뜁니다.");
			return Optional.empty();
		}
		try {
			String body = restClient.get()
				.uri(uriBuilder -> uriBuilder
					.queryParam("key", apiKey)
					.queryParam("q", word)
					.queryParam("req_type", "json")
					.queryParam("method", "exact")
					.build())
				.retrieve()
				.body(String.class);
			if (body == null || body.isBlank()) {
				return Optional.empty();
			}
			StdictSearchResponse response = objectMapper.readValue(body, StdictSearchResponse.class);
			if (!response.hasResult()) {
				return Optional.empty();
			}
			List<String> definitions = response.allDefinitions();
			return Optional.of(definitions.isEmpty() ? List.of("뜻 정보 없음") : definitions);
		} catch (RestClientException | JsonProcessingException e) {
			log.error("표준국어대사전 API 호출에 실패했습니다. word={}", word, e);
			return Optional.empty();
		}
	}
}
