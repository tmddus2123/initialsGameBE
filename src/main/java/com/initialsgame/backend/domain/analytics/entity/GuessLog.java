package com.initialsgame.backend.domain.analytics.entity;

import com.initialsgame.backend.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 단어 제출 통계용 로그. 오늘의 초성과 실제로 초성이 일치하는 시도만 기록한다
 * (중복 제출, 한글이 아닌 입력 등은 통계적으로 의미가 없어 제외).
 */
@Entity
@Getter
@Table(name = "guess_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GuessLog extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 20)
	private String word;

	@Column(nullable = false)
	private boolean correct;

	@Column(name = "chosung_date", nullable = false)
	private LocalDate chosungDate;

	public GuessLog(String word, boolean correct, LocalDate chosungDate) {
		this.word = word;
		this.correct = correct;
		this.chosungDate = chosungDate;
	}
}
