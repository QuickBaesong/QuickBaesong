package com.qb.common.exception;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {
    String getMessage();
    HttpStatus getStatus();
}
