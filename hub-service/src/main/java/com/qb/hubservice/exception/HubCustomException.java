package com.qb.hubservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter

public class HubCustomException extends RuntimeException {


    private final HubErrorCode errorCode;
    private final HttpStatus status;
    private final String message;


    public HubCustomException(HubErrorCode errorCode) {

        super(errorCode.getMessage());


        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
        this.status =  errorCode.getStatus();
    }


    public HubCustomException(HubErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
        this.message = customMessage;
        this.status =  errorCode.getStatus();
    }
}