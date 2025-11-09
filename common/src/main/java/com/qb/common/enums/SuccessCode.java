package com.qb.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessCode {
    OK("요청이 성공적으로 처리되었습니다.",HttpStatus.OK),
    CREATED("리소스가 생성되었습니다.", HttpStatus.CREATED);


    private final String message;
    private final HttpStatus status;

    SuccessCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
