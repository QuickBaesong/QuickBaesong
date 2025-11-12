package com.qb.companyservice.presentation.controller;

import com.qb.common.enums.SuccessCode;
import com.qb.common.response.ApiResponse;
import com.qb.companyservice.application.dto.CompanyCreateRequest;
import com.qb.companyservice.application.dto.CompanyResponse;
import com.qb.companyservice.application.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/v1/company")
@RequiredArgsConstructor
public class CompanyController {

  private final CompanyService companyService;

  /**
   * 업체 생성 API
   * URL: POST /v1/company
   * 권한: HUB_MANAGER, MASTER
   */
  @PostMapping
  public ResponseEntity<ApiResponse<CompanyResponse>> createCompany(
      @Valid @RequestBody CompanyCreateRequest request,
      @RequestHeader("X-User-Id") String userIdHeader) {

    log.info("업체 생성 API 호출: 업체명={}, 타입={}, 허브ID={}",
        request.getCompanyName(), request.getCompanyType(), request.getHubId());

    try {
      // 헤더에서 사용자 ID 추출
      UUID userId = parseUserId(userIdHeader);
      log.debug("요청 사용자 ID: {}", userId);

      // 업체 생성
      CompanyResponse response = companyService.createCompany(request, userId);

      // API 응답 생성 (Common 모듈 활용)
      ApiResponse<CompanyResponse> apiResponse = ApiResponse.of(
          SuccessCode.CREATED, response);

      log.info("업체 생성 성공: ID={}", response.getCompanyId());

      return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);

    } catch (IllegalArgumentException e) {
      log.error("업체 생성 실패 - 유효성 검증 오류: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error("업체 생성 실패 - 시스템 오류", e);
      throw new RuntimeException("업체 생성 중 오류가 발생했습니다", e);
    }
  }

  /**
   * 헤더에서 사용자 ID 파싱
   * 실제 구현에서는 JWT 토큰에서 사용자 정보를 추출해야 함
   */
  private UUID parseUserId(String userIdHeader) {
    if (userIdHeader == null || userIdHeader.trim().isEmpty()) {
      // 실제 구현에서는 인증 정보가 없으면 401 Unauthorized를 반환해야 함
      throw new IllegalArgumentException("X-User-Id 헤더는 필수입니다.");
    }

    try {
      return UUID.fromString(userIdHeader);
    } catch (IllegalArgumentException e) {
      log.warn("유효하지 않은 사용자 ID 헤더: {}", userIdHeader);
      throw new IllegalArgumentException("유효하지 않은 UUID 형식입니다.", e);
    }
  }
}