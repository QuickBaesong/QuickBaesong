package com.qb.companyservice.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CompanyErrorCode {
  NOT_FOUND_COMPANY("업체를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  COMPANY_NAME_ALREADY_EXISTS("이미 존재하는 업체명입니다.", HttpStatus.CONFLICT),
  INVALID_COMPANY_TYPE("유효하지 않은 업체 타입입니다.", HttpStatus.BAD_REQUEST);

  private final String message;
  private final HttpStatus status;

  CompanyErrorCode(String message, HttpStatus status) {
    this.message = message;
    this.status = status;
  }
}