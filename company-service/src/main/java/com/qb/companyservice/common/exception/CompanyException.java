package com.qb.companyservice.common.exception;

import com.qb.companyservice.common.enums.CompanyErrorCode;
import lombok.Getter;

/**
 * Company Service 전용 Exception
 */
@Getter
public class CompanyException extends RuntimeException {

  private final CompanyErrorCode errorCode;

  public CompanyException(CompanyErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public CompanyException(CompanyErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }
}