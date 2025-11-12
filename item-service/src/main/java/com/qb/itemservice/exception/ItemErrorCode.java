package com.qb.itemservice.exception;

import org.springframework.http.HttpStatus;

import com.qb.common.exception.BaseErrorCode;

import lombok.Getter;

@Getter
public enum ItemErrorCode implements BaseErrorCode {

	// -- 상품 --
	INVALID_INPUT("요청이 잘못되었습니다.", HttpStatus.BAD_REQUEST),
	NOT_FOUND_ITEM("해당 상품을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	OUT_OF_STOCK("재고가 부족합니다", HttpStatus.CONFLICT),

	INVALID_HUB("소속 허브에만 상품을 등록할 수 있습니다", HttpStatus.FORBIDDEN),
	INVALID_COMPANY("관리 업체의 상품만을 등록할 수 있습니다", HttpStatus.FORBIDDEN),

	// -- 인증 --
	INVALID_ROLE("해당 권한이 없습니다.", HttpStatus.FORBIDDEN),
	NOT_VALID_TOKEN("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),


	NOT_FOUND_COMPANY("해당 업체를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	NOT_FOUND_HUB("해당 허브를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

	private final String message;
	private final HttpStatus status;

	ItemErrorCode(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
	}
}
