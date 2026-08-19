package com.initialsgame.backend.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

	DAILY_CHOSUNG_NOT_FOUND(HttpStatus.NOT_FOUND, "오늘의 초성 문제를 찾을 수 없습니다."),
	NO_WORDS_AVAILABLE(HttpStatus.INTERNAL_SERVER_ERROR, "출제할 단어가 사전에 없습니다."),
	INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");

	private final HttpStatus status;
	private final String defaultMessage;

	ErrorCode(HttpStatus status, String defaultMessage) {
		this.status = status;
		this.defaultMessage = defaultMessage;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
}
