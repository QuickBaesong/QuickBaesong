package com.qb.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // -- 공통 --
    INVALID_INPUT_VALUE("입력값이 올바르지 않습니다.",HttpStatus.BAD_REQUEST),
    METHOD_NOT_ALLOWED("허용되지 않은 HTTP 메서드입니다.", HttpStatus.METHOD_NOT_ALLOWED),
    INTERNAL_SERVER_ERROR("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    // -- 인증 --
    NOT_VALID_TOKEN("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    // -- 회원 --
    NOT_FOUND_USER("해당 회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    // -- 업체 --
    NOT_FOUND_COMPANY("해당 업체를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    // -- 허브 --

    // -- 상품 --
	NOT_FOUND_ITEM("해당 상품을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	OUT_OF_STOCK("상품 재고가 부족합니다.", HttpStatus.BAD_REQUEST),
	OUT_OF_STOCK_REQUEST_FAILED("상품 재고 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	ITEM_SERVICE_UNAVAILABLE("서비스 오류로 주문할 수 없습니다.", HttpStatus.SERVICE_UNAVAILABLE),

	// -- 배송 --
	DELIVERY_REQUEST_FAILED("배달 생성 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // -- 주문 --
	NOT_FOUND_ORDER("해당 주문을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // -- 알림(슬랙) --

    ;

    private final String message;
    private final HttpStatus status;

    ErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

}
