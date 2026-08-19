package com.initialsgame.backend.common.exception;

import com.initialsgame.backend.common.response.ApiErrorResponse;
import com.initialsgame.backend.common.response.ApiErrorResponse.FieldErrorDetail;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InitialsGameException.class)
	public ResponseEntity<ApiErrorResponse> handleInitialsGameException(InitialsGameException e) {
		ErrorCode errorCode = e.getErrorCode();
		ApiErrorResponse response = ApiErrorResponse.of(
			errorCode.getStatus().value(),
			errorCode.name(),
			e.getMessage()
		);
		return ResponseEntity.status(errorCode.getStatus()).body(response);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
		List<FieldErrorDetail> errors = e.getBindingResult().getFieldErrors().stream()
			.map(fieldError -> new FieldErrorDetail(fieldError.getField(), fieldError.getDefaultMessage()))
			.toList();
		ApiErrorResponse response = ApiErrorResponse.of(
			HttpStatus.BAD_REQUEST.value(),
			ErrorCode.INVALID_REQUEST.name(),
			"입력값이 올바르지 않습니다.",
			errors
		);
		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleException(Exception e) {
		log.error("처리되지 않은 예외가 발생했습니다.", e);
		ApiErrorResponse response = ApiErrorResponse.of(
			HttpStatus.INTERNAL_SERVER_ERROR.value(),
			"INTERNAL_SERVER_ERROR",
			"서버 내부 오류가 발생했습니다."
		);
		return ResponseEntity.internalServerError().body(response);
	}
}
