package com.qb.authservice.exception;

import com.qb.common.exception.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthErrorCode implements BaseErrorCode {
    // -- 인증 --
    INVALID_MASTER_ROLE("MASTER 권한이 필요합니다.", HttpStatus.FORBIDDEN),
    INVALID_HUB_MANAGER_ROLE("HUB_MANAGER 권한이 필요합니다.", HttpStatus.FORBIDDEN),
    NOT_VALID_TOKEN("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),

    // -- 회원 --
    DUPLICATE_ID("이미 사용 중인 아이디입니다.", HttpStatus.CONFLICT),
    NOT_FOUND_USER("해당 회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus status;

    AuthErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

}
