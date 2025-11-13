package com.qb.companyservice.application.service;

import com.qb.companyservice.application.dto.*;
import com.qb.companyservice.domain.entity.Company;
import com.qb.companyservice.domain.service.CompanyDomainService;
import com.qb.companyservice.infrastructure.hub.HubClient;
import com.qb.companyservice.infrastructure.hub.HubClient.HubValidationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {

  private final CompanyDomainService companyDomainService;
  private final HubClient hubClient; // 🆕 HubClient 추가

  // ========== 기존 메서드 ==========

  @Transactional
  public CompanyResponse createCompany(CompanyCreateRequest request, UUID userId) {
    log.info("업체 생성 서비스 시작: 업체명={}", request.getCompanyName());

    // 🆕 허브 검증 추가
    validateHub(request.getHubId());

    Company company = Company.create(
        userId,
        request.getHubId(),
        request.getCompanyName(),
        request.getCompanyType(),
        request.getCompanyAddress()
    );

    Company savedCompany = companyDomainService.save(company);

    log.info("업체 생성 완료: ID={}", savedCompany.getCompanyId());
    return CompanyResponse.from(savedCompany);
  }

  // ========== 새로 추가되는 메서드들 ==========

  /**
   * 특정 업체 조회
   */
  public CompanyResponse getCompany(UUID companyId) {
    log.info("업체 단건 조회: ID={}", companyId);

    Company company = companyDomainService.getByIdAndNotDeleted(companyId);

    log.debug("업체 조회 완료: 업체명={}", company.getCompanyName());
    return CompanyResponse.from(company);
  }

  /**
   * 업체 목록 조회 (검색 + 페이징)
   */
  public CompanyListResponse getCompanies(CompanySearchRequest searchRequest) {
    log.info("업체 목록 조회: 검색조건={}, 페이지={}, 크기={}",
        searchRequest.hasSearchConditions() ? "있음" : "없음",
        searchRequest.getPage(), searchRequest.getSize());

    // 페이징 설정
    Pageable pageable = createPageable(searchRequest);

    // 검색 수행
    Page<Company> companyPage = companyDomainService.searchCompanies(
        searchRequest.getCleanCompanyName(),
        searchRequest.getCompanyType(),
        searchRequest.getHubId(),
        pageable
    );

    // DTO 변환
    Page<CompanyResponse> responsePage = companyPage.map(CompanyResponse::from);

    log.info("업체 목록 조회 완료: 총 {}개, 현재페이지 {}/{}",
        responsePage.getTotalElements(),
        responsePage.getNumber() + 1,
        responsePage.getTotalPages());

    return CompanyListResponse.from(responsePage, searchRequest);
  }

  /**
   * 업체 정보 수정
   */
  @Transactional
  public CompanyResponse updateCompany(UUID companyId, CompanyUpdateRequest request, UUID updatedBy) {
    log.info("업체 수정 시작: ID={}, 변경사항={}", companyId, request.hasAnyChanges() ? "있음" : "없음");

    // 변경사항 확인
    if (!request.hasAnyChanges()) {
      log.warn("업체 수정 요청이지만 변경사항이 없음: ID={}", companyId);
      throw new IllegalArgumentException("수정할 내용이 없습니다.");
    }

    // 기존 업체 정보 조회 (존재 여부 확인)
    Company existingCompany = companyDomainService.getByIdAndNotDeleted(companyId);

    // 업체 정보 수정
    Company updatedCompany = companyDomainService.updateCompany(
        companyId,
        request.getCleanCompanyName(),
        request.getCompanyType(),
        request.getCleanCompanyAddress()
    );

    log.info("업체 수정 완료: ID={}, 업체명={}", updatedCompany.getCompanyId(), updatedCompany.getCompanyName());
    return CompanyResponse.from(updatedCompany);
  }

  /**
   * 업체 삭제
   */
  @Transactional
  public void deleteCompany(UUID companyId, UUID deletedBy) {
    log.info("업체 삭제 시작: ID={}, 삭제자={}", companyId, deletedBy);

    // 업체 존재 여부 확인 후 삭제
    companyDomainService.deleteCompany(companyId, deletedBy.toString());

    log.info("업체 삭제 완료: ID={}", companyId);
  }

  /**
   * 허브별 업체 수 조회
   */
  public long getCompanyCountByHub(UUID hubId) {
    log.debug("허브별 업체 수 조회: 허브ID={}", hubId);
    return companyDomainService.countCompaniesByHubId(hubId);
  }

  // ========== Private Helper 메서드들 ==========

  /**
   * 허브 유효성 검증
   */
  private void validateHub(UUID hubId) {
    log.debug("허브 검증 시작: 허브ID={}", hubId);

    HubValidationResponse response = hubClient.validateHub(hubId);

    if (!response.isExists()) {
      log.warn("허브 검증 실패: {}", response.getMessage());
      throw new IllegalArgumentException(response.getMessage());
    }

    log.debug("허브 검증 성공: {}", response.getHubName());
  }

  /**
   * 검색 요청으로부터 Pageable 객체 생성
   */
  private Pageable createPageable(CompanySearchRequest searchRequest) {
    // 정렬 설정
    Sort sort = Sort.by(
        searchRequest.getValidSortDir().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
        searchRequest.getValidSortBy()
    );

    return PageRequest.of(
        searchRequest.getPage(),
        searchRequest.getLimitedSize(),
        sort
    );
  }

  /**
   * 업체 수정 전 권한 검증 (향후 확장 가능)
   */
  private void validateUpdatePermission(Company company, UUID requestUserId) {
    // 향후 권한 체계가 구현되면 여기서 검증
    // 예: HUB_MANAGER는 자신의 허브 업체만 수정 가능
    log.debug("업체 수정 권한 검증: 업체ID={}, 요청자={}", company.getCompanyId(), requestUserId);
  }

  /**
   * 업체 삭제 전 권한 검증 (향후 확장 가능)
   */
  private void validateDeletePermission(Company company, UUID requestUserId) {
    // 향후 권한 체계가 구현되면 여기서 검증
    // 예: MASTER만 삭제 가능
    log.debug("업체 삭제 권한 검증: 업체ID={}, 요청자={}", company.getCompanyId(), requestUserId);
  }
}