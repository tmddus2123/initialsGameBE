package com.initialsgame.backend.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Render 무료 플랜의 슬립 모드(15분 미사용 시 대기)를 막기 위한 외부 핑 전용 엔드포인트.
 * DB 조회나 통계 로깅 없이 즉시 응답하므로, PageViewLog 접속 통계를 오염시키지 않는다.
 */
@RestController
public class HealthController {

	@GetMapping("/health")
	public String health() {
		return "OK";
	}
}
