package com.qb.companyservice.domain.service;

import com.qb.companyservice.domain.entity.Company;
import com.qb.companyservice.domain.enums.CompanyType;
import com.qb.companyservice.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyDomainService {

  private final CompanyRepository companyRepository;

  // ========== 기존 메서드들 ==========

  /**
   * 업체 정보 저장 (도메인 로직)
   * @param company 저장할 업체 엔티티
   * @return 저장된 업체 엔티티
   */
  @Transactional
  public Company save(Company company) {
    log.debug("업체 정보 저장 시작: {}", company.getCompanyName());

    // 업체명 중복 검증
    validateCompanyNameDuplicate(company.getCompanyName(), null);

    Company savedCompany = companyRepository.save(company);

    log.debug("업체 정보 저장 완료: ID={}, 이름={}",
        savedCompany.getCompanyId(), savedCompany.getCompanyName());

    return savedCompany;
  }

  /**
   * 업체명 중복 검증
   * @param companyName 검증할 업체명
   * @param excludeCompanyId 제외할 업체 ID (수정 시 자기 자신 제외)
   */
  public void validateCompanyNameDuplicate(String companyName, UUID excludeCompanyId) {
    boolean isDuplicate = companyRepository.existsByCompanyNameAndDeletedAtIsNull(
        companyName, excludeCompanyId);

    if (isDuplicate) {
      log.warn("업체명이 이미 존재합니다: {}", companyName);
      throw new IllegalArgumentException("이미 존재하는 업체명입니다: " + companyName);
    }
  }

  // ========== 새로 추가되는 메서드들 ==========

  /**
   * 업체 ID로 조회 (삭제되지 않은 업체만)
   */
  public Optional<Company> findByIdAndNotDeleted(UUID companyId) {
    log.debug("업체 조회: ID={}", companyId);
    return companyRepository.findByIdAndNotDeleted(companyId);
  }

  /**
   * 업체 ID로 조회 (예외 발생 버전)
   */
  public Company getByIdAndNotDeleted(UUID companyId) {
    return findByIdAndNotDeleted(companyId)
        .orElseThrow(() -> {
          log.warn("업체를 찾을 수 없음: ID={}", companyId);
          return new IllegalArgumentException("업체를 찾을 수 없습니다: " + companyId);
        });
  }

  /**
   * 전체 업체 목록 조회 (페이징)
   */
  public Page<Company> findAllCompanies(Pageable pageable) {
    log.debug("전체 업체 목록 조회: 페이지={}", pageable.getPageNumber());
    return companyRepository.findAllActiveCompanies(pageable);
  }

  /**
   * 복합 조건 검색
   */
  public Page<Company> searchCompanies(String companyName, CompanyType companyType,
      UUID hubId, Pageable pageable) {
    log.debug("복합 조건 검색: 업체명={}, 타입={}, 허브ID={}", companyName, companyType, hubId);

    // 모든 조건이 있는 경우
    if (hasValue(companyName) && companyType != null && hubId != null) {
      return companyRepository.findByAllConditionsAndNotDeleted(
          companyName.trim(), companyType, hubId, pageable);
    }
    // 업체명 + 타입
    else if (hasValue(companyName) && companyType != null) {
      return companyRepository.findByCompanyNameAndTypeAndNotDeleted(
          companyName.trim(), companyType, pageable);
    }
    // 업체명 + 허브
    else if (hasValue(companyName) && hubId != null) {
      return companyRepository.findByCompanyNameAndHubIdAndNotDeleted(
          companyName.trim(), hubId, pageable);
    }
    // 타입 + 허브
    else if (companyType != null && hubId != null) {
      return companyRepository.findByCompanyTypeAndHubIdAndNotDeleted(
          companyType, hubId, pageable);
    }
    // 업체명만
    else if (hasValue(companyName)) {
      return companyRepository.findByCompanyNameContainingAndDeletedAtIsNull(
          companyName.trim(), pageable);
    }
    // 타입만
    else if (companyType != null) {
      return companyRepository.findByCompanyTypeAndNotDeleted(companyType, pageable);
    }
    // 허브만
    else if (hubId != null) {
      return companyRepository.findByHubIdAndDeletedAtIsNull(hubId, pageable);
    }
    // 조건 없음 (전체)
    else {
      return findAllCompanies(pageable);
    }
  }

  /**
   * 업체 정보 수정
   */
  @Transactional
  public Company updateCompany(UUID companyId, String newCompanyName,
      CompanyType newCompanyType, String newCompanyAddress) {

    log.debug("업체 수정 시작: ID={}, 새업체명={}", companyId, newCompanyName);

    Company company = getByIdAndNotDeleted(companyId);

    // 업체명 변경 시 중복 검사
    if (newCompanyName != null && !company.getCompanyName().equals(newCompanyName)) {
      validateCompanyNameDuplicate(newCompanyName, companyId);
    }

    // 업체 정보 수정
    company.updateCompanyInfo(newCompanyName, newCompanyType, newCompanyAddress);

    Company updatedCompany = companyRepository.save(company);

    log.info("업체 수정 완료: ID={}, 업체명={}", updatedCompany.getCompanyId(),
        updatedCompany.getCompanyName());

    return updatedCompany;
  }

  /**
   * 업체 논리적 삭제
   */
  @Transactional
  public void deleteCompany(UUID companyId, String deletedBy) {
    log.debug("업체 삭제: ID={}, 삭제자={}", companyId, deletedBy);

    Company company = getByIdAndNotDeleted(companyId);

    // 논리적 삭제 처리
    company.softDelete(deletedBy);

    companyRepository.save(company);

    log.info("업체 삭제 완료: ID={}, 삭제자={}", companyId, deletedBy);
  }

  /**
   * 허브별 업체 수 조회
   */
  public long countCompaniesByHubId(UUID hubId) {
    log.debug("허브별 업체 수 조회: 허브ID={}", hubId);
    return companyRepository.countByHubIdAndNotDeleted(hubId);
  }

  /**
   * 타입별 업체 수 조회
   */
  public long countCompaniesByType(CompanyType companyType) {
    log.debug("타입별 업체 수 조회: 타입={}", companyType);
    return companyRepository.countByCompanyTypeAndNotDeleted(companyType);
  }

  // Helper 메서드
  private boolean hasValue(String str) {
    return str != null && !str.trim().isEmpty();
  }
}