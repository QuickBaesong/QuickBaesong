package com.qb.companyservice.domain.service;

import com.qb.companyservice.domain.entity.Company;
import com.qb.companyservice.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyDomainService {

  private final CompanyRepository companyRepository;

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
   * 업체 ID로 업체 조회
   * @param companyId 업체 ID
   * @return 업체 엔티티
   */
  public Company findById(UUID companyId) {
    log.debug("업체 조회 시작: ID={}", companyId);

    return companyRepository.findByCompanyIdAndDeletedAtIsNull(companyId)
        .orElseThrow(() -> {
          log.warn("업체를 찾을 수 없습니다: ID={}", companyId);
          return new IllegalArgumentException("업체를 찾을 수 없습니다: " + companyId);
        });
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

  /**
   * 특정 허브에 속한 업체 수 조회
   * @param hubId 허브 ID
   * @return 업체 수
   */
  public long countByHubId(UUID hubId) {
    return companyRepository.countByHubIdAndDeletedAtIsNull(hubId);
  }
}