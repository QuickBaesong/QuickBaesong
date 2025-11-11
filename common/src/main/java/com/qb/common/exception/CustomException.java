package com.qb.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException{
    private final BaseErrorCode errorCode;
    private final String message;
    private final HttpStatus status;

    public CustomException(BaseErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
    }
}
