package com.qb.companyservice.presentation.controller;

import com.qb.companyservice.application.dto.CompanyCreateRequest;
import com.qb.companyservice.application.dto.CompanyResponse;
import com.qb.companyservice.application.service.CompanyService;
import com.qb.companyservice.domain.entity.CompanyType;
import com.qb.common.enums.SuccessCode;
import com.qb.common.response.ApiResponse;
import com.qb.common.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

/**
 * 업체 관리 REST Controller
 * 업체 생성, 조회 API를 제공합니다.
 */
@RestController
@RequestMapping("/v1/company")
@RequiredArgsConstructor
@Slf4j
public class CompanyController {

  private final CompanyService companyService;

  /**
   * 업체 생성
   */
  @PostMapping
  public ResponseEntity<ApiResponse<CompanyResponse>> createCompany(
      @Valid @RequestBody CompanyCreateRequest request) {

    log.info("업체 생성 API 호출 - 업체명: {}", request.getCompanyName());

    // TODO: JWT에서 사용자 ID 추출 (현재는 임시)
    String userId = "temp-user-id";

    CompanyResponse response = companyService.createCompany(request, userId);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.of(SuccessCode.CREATED, response));
  }

  /**
   * 특정 업체 조회
   */
  @GetMapping("/{companyId}")
  public ResponseEntity<ApiResponse<CompanyResponse>> getCompany(
      @PathVariable UUID companyId) {

    log.info("업체 조회 API 호출 - ID: {}", companyId);

    CompanyResponse response = companyService.findById(companyId);

    return ResponseEntity.ok(ApiResponse.of(SuccessCode.OK, response));
  }

  /**
   * 업체 목록 조회 (페이징, 검색)
   */
  @GetMapping
  public ResponseEntity<ApiResponse<PageResponse<CompanyResponse>>> getCompanies(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) CompanyType type,
      @RequestParam(required = false) UUID hubId,
      @RequestParam(defaultValue = "companyName") String sortBy,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {

    log.info("업체 목록 조회 API 호출 - name: {}, type: {}, hubId: {}, page: {}, size: {}",
        name, type, hubId, page, size);

    PageResponse<CompanyResponse> response =
        companyService.findCompanies(name, type, hubId, sortBy, page, size);

    return ResponseEntity.ok(ApiResponse.of(SuccessCode.OK, response));
  }
}