package com.qb.common.enums;

import com.qb.common.exception.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode implements BaseErrorCode {
    // -- 공통 --
    INVALID_INPUT_VALUE("입력값이 올바르지 않습니다.",HttpStatus.BAD_REQUEST),
    METHOD_NOT_ALLOWED("허용되지 않은 HTTP 메서드입니다.", HttpStatus.METHOD_NOT_ALLOWED),
    INTERNAL_SERVER_ERROR("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // -- 인증 --
    UNAUTHORIZED("인증 정보가 없습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("토큰이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN("토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_USER("사용자 정보가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED),
    USER_CONTEXT_NOT_SET("UserContext가 설정되지 않았습니다.", HttpStatus.NOT_FOUND);


    private final String message;
    private final HttpStatus status;

    ErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

}
