package com.qb.companyservice.common.exception;

import com.qb.companyservice.common.enums.CompanyErrorCode;
import com.qb.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Company Service 전용 예외 처리기
 */
@RestControllerAdvice
@Component
@Order(1)  // 우선순위 설정
@Slf4j
public class CompanyExceptionHandler {

  /**
   * CompanyException 처리
   */
  @ExceptionHandler(CompanyException.class)
  public ResponseEntity<ApiResponse<Void>> handleCompanyException(CompanyException e) {
    log.warn("CompanyException 발생: {}", e.getMessage());

    CompanyErrorCode errorCode = e.getErrorCode();

    // ErrorCode를 ApiResponse에 맞는 형태로 변환
    ApiResponse<Void> response = ApiResponse.<Void>builder()
        .message(errorCode.getMessage())
        .code(null)
        .data(null)
        .build();

    return ResponseEntity
        .status(errorCode.getStatus())
        .body(response);
  }
}