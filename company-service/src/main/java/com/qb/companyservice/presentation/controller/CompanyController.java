package com.qb.companyservice.presentation.controller;

import com.qb.common.enums.SuccessCode;
import com.qb.common.response.ApiResponse;
import com.qb.companyservice.application.dto.*;
import com.qb.companyservice.application.service.CompanyService;
import com.qb.companyservice.domain.enums.CompanyType;
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
      UUID userId = parseUserId(userIdHeader);
      log.debug("요청 사용자 ID: {}", userId);

      CompanyResponse response = companyService.createCompany(request, userId);
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
   * 특정 업체 정보 조회 API
   * URL: GET /v1/company/{company_id}
   * 권한: COMPANY_MANAGER, DELIVERY_MANAGER, HUB_MANAGER, MASTER
   */
  @GetMapping("/{companyId}")
  public ResponseEntity<ApiResponse<CompanyResponse>> getCompany(
      @PathVariable UUID companyId,
      @RequestHeader("X-User-Id") String userIdHeader) {

    log.info("특정 업체 조회 API 호출: 업체ID={}", companyId);

    try {
      UUID userId = parseUserId(userIdHeader);
      log.debug("요청 사용자 ID: {}", userId);

      CompanyResponse response = companyService.getCompany(companyId);
      ApiResponse<CompanyResponse> apiResponse = ApiResponse.of(
          SuccessCode.OK, response);

      log.info("특정 업체 조회 성공: 업체ID={}, 업체명={}",
          response.getCompanyId(), response.getCompanyName());

      return ResponseEntity.ok(apiResponse);

    } catch (IllegalArgumentException e) {
      log.error("특정 업체 조회 실패 - 업체를 찾을 수 없음: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error("특정 업체 조회 실패 - 시스템 오류", e);
      throw new RuntimeException("업체 조회 중 오류가 발생했습니다", e);
    }
  }

  /**
   * 업체 정보 전체 조회 (검색 및 페이징) API
   * URL: GET /v1/company?name={}&type={}&hubId={}&sortBy={}&size={}&page={}
   * 권한: COMPANY_MANAGER, DELIVERY_MANAGER, HUB_MANAGER, MASTER
   */
  @GetMapping
  public ResponseEntity<ApiResponse<CompanyListResponse>> getCompanies(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String type,
      @RequestParam(required = false) UUID hubId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "company_name") String sortBy,
      @RequestHeader("X-User-Id") String userIdHeader) {

    log.info("업체 목록 조회 API 호출: 업체명={}, 타입={}, 허브ID={}, 페이지={}, 크기={}",
        name, type, hubId, page, size);

    try {
      UUID userId = parseUserId(userIdHeader);
      log.debug("요청 사용자 ID: {}", userId);

      CompanySearchRequest searchRequest = CompanySearchRequest.builder()
          .companyName(name)
          .companyType(parseCompanyType(type))
          .hubId(hubId)
          .page(page)
          .size(size)
          .sortBy(mapSortByField(sortBy))
          .sortDir("desc")
          .build();

      CompanyListResponse response = companyService.getCompanies(searchRequest);
      ApiResponse<CompanyListResponse> apiResponse = ApiResponse.of(
          SuccessCode.OK, response);

      log.info("업체 목록 조회 성공: {}", response.getSummary());
      return ResponseEntity.ok(apiResponse);

    } catch (IllegalArgumentException e) {
      log.error("업체 목록 조회 실패 - 유효성 검증 오류: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error("업체 목록 조회 실패 - 시스템 오류", e);
      throw new RuntimeException("업체 목록 조회 중 오류가 발생했습니다", e);
    }
  }

  /**
   * 업체 정보 수정 API
   * URL: PUT /v1/company/{company_id}
   * 권한: DELIVERY_MANAGER, HUB_MANAGER, MASTER
   */
  @PutMapping("/{companyId}")
  public ResponseEntity<ApiResponse<CompanyResponse>> updateCompany(
      @PathVariable UUID companyId,
      @Valid @RequestBody CompanyUpdateRequest request,
      @RequestHeader("X-User-Id") String userIdHeader) {

    log.info("업체 수정 API 호출: 업체ID={}, 변경사항 있음={}",
        companyId, request.hasAnyChanges());

    try {
      UUID userId = parseUserId(userIdHeader);
      log.debug("요청 사용자 ID: {}", userId);

      CompanyResponse response = companyService.updateCompany(companyId, request, userId);
      ApiResponse<CompanyResponse> apiResponse = ApiResponse.of(
          SuccessCode.OK, response);

      log.info("업체 수정 성공: 업체ID={}, 업체명={}",
          response.getCompanyId(), response.getCompanyName());

      return ResponseEntity.ok(apiResponse);

    } catch (IllegalArgumentException e) {
      log.error("업체 수정 실패 - 유효성 검증 오류: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error("업체 수정 실패 - 시스템 오류", e);
      throw new RuntimeException("업체 수정 중 오류가 발생했습니다", e);
    }
  }

  /**
   * 업체 정보 삭제 API
   * URL: DELETE /v1/company/{company_id}
   * 권한: HUB_MANAGER, MASTER
   */
  @DeleteMapping("/{companyId}")
  public ResponseEntity<ApiResponse<Object>> deleteCompany(
      @PathVariable UUID companyId,
      @RequestHeader("X-User-Id") String userIdHeader) {

    log.info("업체 삭제 API 호출: 업체ID={}", companyId);

    try {
      UUID userId = parseUserId(userIdHeader);
      log.debug("요청 사용자 ID: {}", userId);

      companyService.deleteCompany(companyId, userId);
      ApiResponse<Object> apiResponse = ApiResponse.of(
          SuccessCode.OK, "업체 정보가 성공적으로 삭제되었습니다.");

      log.info("업체 삭제 성공: 업체ID={}", companyId);
      return ResponseEntity.ok(apiResponse);

    } catch (IllegalArgumentException e) {
      log.error("업체 삭제 실패 - 업체를 찾을 수 없음: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error("업체 삭제 실패 - 시스템 오류", e);
      throw new RuntimeException("업체 삭제 중 오류가 발생했습니다", e);
    }
  }

  // ========== Private Helper 메서드 ==========

  /**
   * 헤더에서 사용자 ID 파싱
   */
  private UUID parseUserId(String userIdHeader) {
    if (userIdHeader == null || userIdHeader.trim().isEmpty()) {
      throw new IllegalArgumentException("X-User-Id 헤더는 필수입니다.");
    }

    try {
      return UUID.fromString(userIdHeader);
    } catch (IllegalArgumentException e) {
      log.warn("유효하지 않은 사용자 ID 헤더: {}", userIdHeader);
      throw new IllegalArgumentException("유효하지 않은 UUID 형식입니다.", e);
    }
  }

  /**
   * type 문자열을 CompanyType enum으로 변환
   */
  private CompanyType parseCompanyType(String type) {
    if (type == null || type.trim().isEmpty()) {
      return null;
    }

    try {
      return CompanyType.valueOf(type.toUpperCase());
    } catch (IllegalArgumentException e) {
      log.warn("유효하지 않은 업체 타입: {}", type);
      throw new IllegalArgumentException("유효하지 않은 업체 타입입니다: " + type + ". (SENDER, RECEIVER 중 선택)");
    }
  }

  /**
   * sortBy 필드명 매핑 (명세서의 company_name을 companyName으로)
   */
  private String mapSortByField(String sortBy) {
    if (sortBy == null) {
      return "companyName";
    }

    return switch (sortBy.toLowerCase()) {
      case "company_name" -> "companyName";
      case "created_at" -> "createdAt";
      case "updated_at" -> "updatedAt";
      case "company_type" -> "companyType";
      case "company_address" -> "companyAddress";
      case "hub_id" -> "hubId";
      default -> "companyName";
    };
  }
}