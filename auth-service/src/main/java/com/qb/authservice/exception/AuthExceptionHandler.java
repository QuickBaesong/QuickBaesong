package com.qb.authservice.exception;

import com.qb.common.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.qb.authservice")
public class AuthExceptionHandler {

    @ExceptionHandler(AuthCustomException.class)
    public ResponseEntity<?> handleAuthException(AuthCustomException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorResponse(e.getErrorCode()));
    }
}