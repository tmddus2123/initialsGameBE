package com.initialsgame.backend.domain.word.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StdictSearchResponse(Channel channel) {

	public boolean hasResult() {
		return channel != null && channel.hasResult();
	}

	/**
	 * 동음이의어별로 뜻풀이가 여러 개일 수 있어 검색된 항목의 뜻풀이를 모두 모은다.
	 */
	public List<String> allDefinitions() {
		if (channel == null || channel.item() == null) {
			return List.of();
		}
		return channel.item().stream()
			.map(Item::sense)
			.filter(sense -> sense != null && sense.definition() != null && !sense.definition().isBlank())
			.map(Sense::definition)
			.distinct()
			.toList();
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Channel(String total, List<Item> item) {

		public boolean hasResult() {
			return total != null && !total.isBlank() && !"0".equals(total);
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Item(String word, Sense sense) {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Sense(String definition) {
	}
}
