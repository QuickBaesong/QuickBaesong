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
  // private final HubClient hubClient; // 추후 구현

  @Override
  @Transactional
  public CompanyResponse createCompany(CompanyCreateRequest request, String userId) {
    log.info("업체 생성 요청 - 사용자: {}, 업체명: {}", userId, request.getCompanyName());

    // TODO: Hub 존재 여부 확인
    // validateHubExists(request.getHubId());

    // TODO: 사용자 권한 확인 (현재는 스킵)

    // 임시 UUID 생성 (userId를 UUID로 변환하는 대신)
    UUID userUuid;
    try {
      userUuid = UUID.fromString(userId);
    } catch (IllegalArgumentException e) {
      // 유효하지 않은 UUID면 임시로 랜덤 UUID 생성
      userUuid = UUID.randomUUID();
      log.warn("유효하지 않은 userId: {}, 임시 UUID 사용: {}", userId, userUuid);
    }

    // 도메인 서비스를 통한 업체 생성
    Company company = companyDomainService.createCompany(
        userUuid,
        request.getHubId(),
        request.getCompanyName(),
        request.getCompanyType(),
        request.getCompanyAddress()
    );

    // 저장
    Company savedCompany = companyRepository.save(company);

    log.info("업체 생성 완료 - ID: {}, 업체명: {}", savedCompany.getCompanyId(), savedCompany.getCompanyName());

    return CompanyResponse.from(savedCompany);
  }

  @Override
  public CompanyResponse findById(UUID companyId) {
    log.info("업체 조회 요청 - ID: {}", companyId);

    Company company = companyRepository.findByCompanyIdAndDeletedAtIsNull(companyId)
        .orElseThrow(() -> new CompanyException(CompanyErrorCode.NOT_FOUND_COMPANY));

    log.info("업체 조회 완료 - 업체명: {}", company.getCompanyName());

    return CompanyResponse.from(company);
  }

  @Override
  public PageResponse<CompanyResponse> findCompanies(String name, CompanyType type, UUID hubId,
      String sortBy, int page, int size) {
    log.info("업체 목록 조회 요청 - name: {}, type: {}, hubId: {}, page: {}, size: {}",
        name, type, hubId, page, size);

    // 정렬 설정
    Sort sort = Sort.by(Sort.Direction.ASC, sortBy != null ? sortBy : "companyName");
    Pageable pageable = PageRequest.of(page, size, sort);

    // 검색 실행
    Page<Company> companies = companyRepository.findCompaniesWithFilters(name, type, hubId, pageable);

    // DTO 변환
    Page<CompanyResponse> responses = companies.map(CompanyResponse::from);

    log.info("업체 목록 조회 완료 - 총 {}건, 현재 페이지: {}/{}",
        responses.getTotalElements(), page + 1, responses.getTotalPages());

    return PageResponse.from(responses);
  }

  // 추후 구현 예정
  // private void validateHubExists(UUID hubId) {
  //     if (!hubClient.existsById(hubId)) {
  //         throw new CustomException(ErrorCode.NOT_FOUND_HUB);
  //     }
  // }
}