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
 * "오늘의 초성" 조회(페이지 접속) 시각을 기록하는 로그.
 */
@Entity
@Getter
@Table(name = "page_view_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageViewLog extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "chosung_date", nullable = false)
	private LocalDate chosungDate;

	public PageViewLog(LocalDate chosungDate) {
		this.chosungDate = chosungDate;
	}
}
