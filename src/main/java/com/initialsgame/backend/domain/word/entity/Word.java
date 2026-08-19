package com.initialsgame.backend.domain.word.entity;

import com.initialsgame.backend.common.BaseTimeEntity;
import com.initialsgame.backend.common.util.HangulUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "words")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Word extends BaseTimeEntity {

	private static final String DEFINITION_DELIMITER = "\n";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 20)
	private String text;

	@Column(nullable = false, length = 20)
	private String initials;

	@Column(name = "syllable_count", nullable = false)
	private int syllableCount;

	/**
	 * 동음이의어별 뜻풀이를 {@value #DEFINITION_DELIMITER}로 구분해 저장한다.
	 * {@link #getDefinitions()}로 조회한다.
	 */
	@Getter(AccessLevel.NONE)
	@Column(name = "definition", columnDefinition = "TEXT")
	private String definitions;

	public Word(String text) {
		this.text = text;
		this.initials = HangulUtils.extractInitials(text);
		this.syllableCount = text.length();
	}

	public Word(String text, List<String> definitions) {
		this(text);
		this.definitions = String.join(DEFINITION_DELIMITER, definitions);
	}

	public List<String> getDefinitions() {
		if (definitions == null || definitions.isBlank()) {
			return List.of();
		}
		return Arrays.asList(definitions.split(DEFINITION_DELIMITER));
	}

	public void updateDefinitions(List<String> definitions) {
		this.definitions = String.join(DEFINITION_DELIMITER, definitions);
	}
}
