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
      @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {

    log.info("업체 생성 API 호출: 업체명={}, 타입={}, 허브ID={}",
        request.getCompanyName(), request.getCompanyType(), request.getHubId());

    try {
      // 헤더에서 사용자 ID 추출 (임시로 헤더 사용, 실제로는 JWT에서 추출)
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
   * 업체 단건 조회 API
   * URL: GET /v1/company/{companyId}
   * 권한: COMPANY_MANAGER, DELIVERY_MANAGER, HUB_MANAGER, MASTER
   */
  @GetMapping("/{companyId}")
  public ResponseEntity<ApiResponse<CompanyResponse>> getCompany(
      @PathVariable("companyId") UUID companyId) {

    log.info("업체 단건 조회 API 호출: ID={}", companyId);

    try {
      CompanyResponse response = companyService.getCompany(companyId);

      ApiResponse<CompanyResponse> apiResponse = ApiResponse.of(
          SuccessCode.OK, response);

      log.info("업체 조회 성공: ID={}, 이름={}", response.getCompanyId(), response.getCompanyName());

      return ResponseEntity.ok(apiResponse);

    } catch (IllegalArgumentException e) {
      log.error("업체 조회 실패 - 업체를 찾을 수 없음: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error("업체 조회 실패 - 시스템 오류", e);
      throw new RuntimeException("업체 조회 중 오류가 발생했습니다", e);
    }
  }

  /**
   * 허브별 업체 수 조회 API (추가 기능)
   * URL: GET /v1/company/count/hub/{hubId}
   */
  @GetMapping("/count/hub/{hubId}")
  public ResponseEntity<ApiResponse<Long>> getCompanyCountByHub(
      @PathVariable("hubId") UUID hubId) {

    log.info("허브별 업체 수 조회 API 호출: 허브ID={}", hubId);

    try {
      long count = companyService.getCompanyCountByHub(hubId);

      ApiResponse<Long> apiResponse = ApiResponse.of(SuccessCode.OK, count);

      log.info("허브별 업체 수 조회 성공: 허브ID={}, 업체수={}", hubId, count);

      return ResponseEntity.ok(apiResponse);

    } catch (Exception e) {
      log.error("허브별 업체 수 조회 실패", e);
      throw new RuntimeException("허브별 업체 수 조회 중 오류가 발생했습니다", e);
    }
  }

  /**
   * 헤더에서 사용자 ID 파싱 (임시 구현)
   * 실제로는 JWT 토큰에서 사용자 정보를 추출해야 함
   */
  private UUID parseUserId(String userIdHeader) {
    if (userIdHeader == null || userIdHeader.trim().isEmpty()) {
      log.warn("사용자 ID 헤더가 없음, 임시 UUID 생성");
      return UUID.randomUUID();
    }

    try {
      return UUID.fromString(userIdHeader);
    } catch (IllegalArgumentException e) {
      log.warn("유효하지 않은 사용자 ID 헤더: {}, 임시 UUID 생성", userIdHeader);
      return UUID.randomUUID();
    }
  }
}