package com.qb.hubservice.exception;

import com.qb.common.exception.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum HubErrorCode implements BaseErrorCode {

    INVALID_HUB_ID(HttpStatus.BAD_REQUEST, "hubId: 기존 허브 ID를 찾을 수 없습니다."),
    INVALID_START_HUB_ID(HttpStatus.BAD_REQUEST, "startHubId: 시작 허브 ID를 찾을 수 없습니다."),
    INVALID_DESTINATION_HUB_ID(HttpStatus.BAD_REQUEST, "destinationHubId: 도착 허브 ID를 찾을 수 없습니다."),


    DUPLICATE_HUB_ROUTE(HttpStatus.CONFLICT, "동일한 경로의 허브 루트가 이미 존재합니다."),


    HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 허브를 찾을 수 없습니다.");


    @Getter
    private final HttpStatus status;
    @Getter
    private final String message;

    HubErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
