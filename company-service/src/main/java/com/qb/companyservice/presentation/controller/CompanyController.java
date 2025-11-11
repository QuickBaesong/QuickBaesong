package com.qb.companyservice.presentation.controller;

import com.qb.companyservice.application.dto.CompanyCreateRequest;
import com.qb.companyservice.application.dto.CompanyResponse;
import com.qb.companyservice.application.service.CompanyService;
import com.qb.companyservice.domain.entity.CompanyType;
import com.qb.common.enums.SuccessCode;
import com.qb.common.response.ApiResponse;
import com.qb.common.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * 업체 관리 REST Controller
 * 업체 생성, 조회, 목록 조회 API를 제공합니다.
 */
@RestController
@RequestMapping("/v1/company")
@RequiredArgsConstructor
@Slf4j
public class CompanyController {

  private final CompanyService companyService;

  /**
   * 업체 생성 API
   * @param request 업체 생성 요청 데이터
   * @param userId 요청자 사용자 ID (헤더에서 전달)
   * @return 생성된 업체 정보
   */
  @PostMapping
  public ResponseEntity<ApiResponse<CompanyResponse>> createCompany(
      @Valid @RequestBody CompanyCreateRequest request,
      @RequestHeader(value = "X-User-Id", required = true) String userId) {

    log.info("업체 생성 API 호출 - 요청자: {}, 업체명: {}", userId, request.getCompanyName());

    CompanyResponse response = companyService.createCompany(request, userId);

    // ✅ SuccessCode enum 사용
    ApiResponse<CompanyResponse> apiResponse = ApiResponse.<CompanyResponse>builder()
        .message("리소스가 생성되었습니다.")
        .code(SuccessCode.CREATED)  // 🔧 String → SuccessCode로 수정
        .data(response)
        .build();

    log.info("업체 생성 API 완료 - 업체 ID: {}", response.getCompanyId());

    return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
  }

  /**
   * 업체 단건 조회 API
   * @param companyId 조회할 업체 ID
   * @return 업체 상세 정보
   */
  @GetMapping("/{companyId}")
  public ResponseEntity<ApiResponse<CompanyResponse>> getCompany(
      @PathVariable("companyId") UUID companyId) {

    log.info("업체 조회 API 호출 - 업체 ID: {}", companyId);

    CompanyResponse response = companyService.findById(companyId);

    // ✅ SuccessCode enum 사용
    ApiResponse<CompanyResponse> apiResponse = ApiResponse.<CompanyResponse>builder()
        .message("요청이 성공적으로 처리되었습니다.")
        .code(SuccessCode.OK)  // 🔧 String → SuccessCode로 수정
        .data(response)
        .build();

    log.info("업체 조회 API 완료 - 업체명: {}", response.getCompanyName());

    return ResponseEntity.ok(apiResponse);
  }

  /**
   * 업체 목록 조회 API (페이징, 검색, 정렬 지원)
   * @param name 업체명 검색 키워드 (선택)
   * @param type 업체 타입 필터 (선택)
   * @param hubId 허브 ID 필터 (선택)
   * @param sortBy 정렬 기준 필드 (기본값: createdAt)
   * @param page 페이지 번호 (기본값: 0)
   * @param size 페이지 크기 (기본값: 20)
   * @return 업체 목록 (페이징 정보 포함)
   */
  @GetMapping
  public ResponseEntity<ApiResponse<PageResponse<CompanyResponse>>> getCompanies(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) CompanyType type,
      @RequestParam(required = false) UUID hubId,
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {

    log.info("업체 목록 조회 API 호출 - name: {}, type: {}, hubId: {}, page: {}, size: {}, sortBy: {}",
        name, type, hubId, page, size, sortBy);

    PageResponse<CompanyResponse> response = companyService.findCompanies(name, type, hubId, sortBy, page, size);

    // ✅ SuccessCode enum 사용
    ApiResponse<PageResponse<CompanyResponse>> apiResponse = ApiResponse.<PageResponse<CompanyResponse>>builder()
        .message("요청이 성공적으로 처리되었습니다.")
        .code(SuccessCode.OK)  // 🔧 String → SuccessCode로 수정
        .data(response)
        .build();

    log.info("업체 목록 조회 API 완료 - 총 {}건, 현재 페이지: {}/{}",
        response.getTotalElements(), page + 1, response.getTotalPages());

    return ResponseEntity.ok(apiResponse);
  }
}