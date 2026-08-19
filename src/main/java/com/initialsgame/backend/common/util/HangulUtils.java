package com.initialsgame.backend.common.util;

public final class HangulUtils {

	private static final char[] INITIALS = {
		'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ',
		'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
	};

	private static final int HANGUL_BASE = 0xAC00;
	private static final int HANGUL_LAST = 0xD7A3;
	private static final int MEDIAL_COUNT = 21;
	private static final int FINAL_COUNT = 28;

	private HangulUtils() {
	}

	public static String extractInitials(String text) {
		StringBuilder sb = new StringBuilder(text.length());
		for (char c : text.toCharArray()) {
			if (c < HANGUL_BASE || c > HANGUL_LAST) {
				throw new IllegalArgumentException("한글 완성형 문자만 지원합니다: '" + c + "'");
			}
			int syllableIndex = (c - HANGUL_BASE) / (MEDIAL_COUNT * FINAL_COUNT);
			sb.append(INITIALS[syllableIndex]);
		}
		return sb.toString();
	}
}
