package com.qb.common.response;

import com.qb.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private int status;
    private ErrorCode errorCode;
    private String message;


    public ErrorResponse(ErrorCode errorCode){
        this.status = errorCode.getStatus().value();
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

}
