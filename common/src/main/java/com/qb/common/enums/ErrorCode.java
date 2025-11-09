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
    // -- 배송 --
    // -- 주문 --
    // -- 알림(슬랙) --

    ;

    private final String message;
    private final HttpStatus status;

    ErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

}
