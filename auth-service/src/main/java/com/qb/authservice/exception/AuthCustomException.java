package com.qb.authservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthCustomException extends RuntimeException {

    private final AuthErrorCode errorCode;
    private final String message;
    private final HttpStatus status;

    public AuthCustomException(AuthErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
    }
}
