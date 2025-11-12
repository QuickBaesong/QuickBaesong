package com.qb.orderservice.exception;

import org.springframework.http.HttpStatus;

import com.qb.common.exception.BaseErrorCode;

import lombok.Getter;

@Getter
public enum OrderErrorCode implements BaseErrorCode {
	// 배송
	INVALID_DELIVERY_REQUEST("배송 요청 입력값이 유효하지 않습니다.",HttpStatus.BAD_REQUEST),
	DELIVERY_SERVICE_UNAVAILABLE("배송 서비스에 연결할 수 없습니다.",HttpStatus.SERVICE_UNAVAILABLE),

	// 상품
	OUT_OF_STOCK("재고가 부족합니다", HttpStatus.CONFLICT),
	NOT_FOUND_ITEM("해당 상품을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
	ITEM_SERVICE_UNAVAILABLE("상품 서비스에 연결할 수 없습니다.",HttpStatus.SERVICE_UNAVAILABLE),

	// 주문, 주문상품
	INVALID_ORDER_REQUEST("주문 요청 입력값이 유효하지 않습니다.",HttpStatus.BAD_REQUEST),
	NOT_FOUND_ORDER("해당 주문을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
	NOT_FOUND_ORDER_ITEM("주문하지 않은 상품입니다", HttpStatus.CONFLICT),
	ORDER_CREATION_FAILED("주문 생성에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

	ORDER_ACCESS_DENIED("해당 주문에 대한 접근 권한이 없습니다", HttpStatus.FORBIDDEN);

	private final String message;
	private final HttpStatus status;

	OrderErrorCode(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
	}
}
