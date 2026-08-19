package com.initialsgame.backend.domain.chosung.entity;

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

@Entity
@Getter
@Table(name = "daily_chosungs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyChosung extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private LocalDate date;

	@Column(nullable = false, length = 20)
	private String initials;

	@Column(name = "syllable_count", nullable = false)
	private int syllableCount;

	public DailyChosung(LocalDate date, String initials, int syllableCount) {
		this.date = date;
		this.initials = initials;
		this.syllableCount = syllableCount;
	}
}
