package com.qb.common.response;

import com.qb.common.exception.BaseErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private int status;
    private BaseErrorCode errorCode;
    private String message;


    public ErrorResponse(BaseErrorCode errorCode){
        this.status = errorCode.getStatus().value();
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public ErrorResponse(BaseErrorCode errorCode, String customMessage) {
        this.status = errorCode.getStatus().value();
        this.errorCode = errorCode;
        this.message = customMessage;
    }
}
