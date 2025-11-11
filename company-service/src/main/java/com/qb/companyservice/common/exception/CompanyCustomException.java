package com.qb.companyservice.common.exception;

import com.qb.companyservice.common.enums.CompanyErrorCode;
import lombok.Getter;

/**
 * Company Service 전용 Custom Exception
 */
@Getter
public class CompanyCustomException extends RuntimeException {

  private final CompanyErrorCode errorCode;

  public CompanyCustomException(CompanyErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public CompanyCustomException(CompanyErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }
}