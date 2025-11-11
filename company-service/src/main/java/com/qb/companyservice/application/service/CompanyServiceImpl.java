package com.qb.companyservice.application.service;

import com.qb.companyservice.application.dto.CompanyCreateRequest;
import com.qb.companyservice.application.dto.CompanyResponse;
import com.qb.companyservice.common.enums.CompanyErrorCode;
import com.qb.companyservice.common.exception.CompanyException;
import com.qb.companyservice.domain.entity.Company;
import com.qb.companyservice.domain.entity.CompanyType;
import com.qb.companyservice.domain.repository.CompanyRepository;
import com.qb.companyservice.domain.service.CompanyDomainService;
import com.qb.common.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

/**
 * 업체 Application Service 구현체
 * 업체 관련 유스케이스를 구현합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CompanyServiceImpl implements CompanyService {

  private final CompanyRepository companyRepository;
  private final CompanyDomainService companyDomainService;

  // 🔒 정렬 가능한 필드 목록 (보안을 위한 화이트리스트)
  private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
      "companyName", "companyType", "createdAt", "updatedAt", "hubId"
  );

  @Override
  @Transactional
  public CompanyResponse createCompany(CompanyCreateRequest request, String userId) {
    log.info("업체 생성 요청 - 사용자: {}, 업체명: {}", userId, request.getCompanyName());

    // 🛡️ UUID 파싱 예외 처리 강화
    UUID userUuid;
    try {
      userUuid = UUID.fromString(userId);
    } catch (IllegalArgumentException e) {
      log.warn("유효하지 않은 userId: {}. 업체 생성을 중단합니다.", userId);
      throw new IllegalArgumentException("유효하지 않은 형식의 사용자 ID입니다.");
    }

    // ✨ DTO에서 Entity로 변환 (팀원 제안 반영)
    Company company = request.toEntity(userUuid);

    // 도메인 서비스를 통한 비즈니스 규칙 검증
    Company validatedCompany = companyDomainService.validateAndPrepareCompany(company);

    // 저장
    Company savedCompany = companyRepository.save(validatedCompany);

    log.info("업체 생성 완료 - ID: {}, 업체명: {}", savedCompany.getCompanyId(), savedCompany.getCompanyName());

    return CompanyResponse.from(savedCompany);
  }

  @Override
  public CompanyResponse findById(UUID companyId) {
    log.info("업체 조회 요청 - ID: {}", companyId);

    // ✅ 수정된 메서드명 사용
    Company company = companyRepository.findByCompanyIdAndDeletedAtIsNull(companyId)
        .orElseThrow(() -> new CompanyException(CompanyErrorCode.NOT_FOUND_COMPANY));

    log.info("업체 조회 완료 - 업체명: {}", company.getCompanyName());

    return CompanyResponse.from(company);
  }

  @Override
  public PageResponse<CompanyResponse> findCompanies(String name, CompanyType type, UUID hubId,
      String sortBy, int page, int size) {
    log.info("업체 목록 조회 요청 - name: {}, type: {}, hubId: {}, page: {}, size: {}, sortBy: {}",
        name, type, hubId, page, size, sortBy);

    // 🔒 정렬 필드 검증 (보안 강화)
    String validatedSortBy = validateSortField(sortBy);

    // 정렬 설정
    Sort sort = Sort.by(Sort.Direction.DESC, validatedSortBy);
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<Company> companies;

    // ✅ 수정된 Repository 메서드들 사용
    if (name != null && !name.trim().isEmpty()) {
      companies = companyRepository.findByDeletedAtIsNullAndCompanyNameContainingIgnoreCase(name, pageable);
    } else if (type != null) {
      companies = companyRepository.findByDeletedAtIsNullAndCompanyType(type, pageable);
    } else if (hubId != null) {
      companies = companyRepository.findByDeletedAtIsNullAndHubId(hubId, pageable);
    } else {
      // 전체 조회는 동적 쿼리 사용 (더 효율적)
      companies = companyRepository.findCompaniesWithFilters(null, null, null, pageable);
    }

    // DTO 변환
    Page<CompanyResponse> responses = companies.map(CompanyResponse::from);

    log.info("업체 목록 조회 완료 - 총 {}건, 현재 페이지: {}/{}, 정렬: {}",
        responses.getTotalElements(), page + 1, responses.getTotalPages(), validatedSortBy);

    return PageResponse.from(responses);
  }

  /**
   * 🔒 정렬 필드 유효성 검증
   * @param sortBy 요청된 정렬 필드
   * @return 검증된 정렬 필드
   */
  private String validateSortField(String sortBy) {
    if (sortBy == null || sortBy.trim().isEmpty()) {
      return "createdAt"; // 기본 정렬: 최신순
    }

    String trimmedSortBy = sortBy.trim();
    if (ALLOWED_SORT_FIELDS.contains(trimmedSortBy)) {
      return trimmedSortBy;
    }

    log.warn("허용되지 않은 정렬 필드 요청: {}. 기본 정렬(createdAt) 사용.", sortBy);
    return "createdAt";
  }
}