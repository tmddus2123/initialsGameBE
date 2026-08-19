package com.initialsgame.backend.common.exception;

import lombok.Getter;

@Getter
public class InitialsGameException extends RuntimeException {

	private final ErrorCode errorCode;

	public InitialsGameException(ErrorCode errorCode) {
		super(errorCode.getDefaultMessage());
		this.errorCode = errorCode;
	}

	public InitialsGameException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
}
