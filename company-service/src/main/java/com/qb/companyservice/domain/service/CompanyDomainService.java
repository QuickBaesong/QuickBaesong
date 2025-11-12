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

  // delete() 메서드와 findById() 메서드 제거 - 지금은 생성 API만 필요
}