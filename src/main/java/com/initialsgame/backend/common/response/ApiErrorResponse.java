package com.initialsgame.backend.common.response;

import java.time.LocalDateTime;
import java.util.List;

public record ApiErrorResponse(
	LocalDateTime timestamp,
	int status,
	String code,
	String message,
	List<FieldErrorDetail> errors
) {

	public static ApiErrorResponse of(int status, String code, String message) {
		return new ApiErrorResponse(LocalDateTime.now(), status, code, message, List.of());
	}

	public static ApiErrorResponse of(int status, String code, String message, List<FieldErrorDetail> errors) {
		return new ApiErrorResponse(LocalDateTime.now(), status, code, message, errors);
	}

	public record FieldErrorDetail(String field, String reason) {
	}
}
