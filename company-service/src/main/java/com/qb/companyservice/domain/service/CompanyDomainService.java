package com.qb.companyservice.domain.service;

import com.qb.companyservice.domain.entity.Company;
import com.qb.companyservice.domain.entity.CompanyType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 업체 도메인 서비스
 * 복잡한 비즈니스 로직이 필요할 때 사용
 */
@Service
@Slf4j
public class CompanyDomainService {

  /**
   * 업체 생성 시 비즈니스 규칙 검증
   * @param company 생성할 업체
   * @return 검증된 업체
   */
  public Company validateAndPrepareCompany(Company company) {
    // 비즈니스 규칙 검증 로직
    validateCompanyName(company.getCompanyName());
    validateCompanyType(company.getCompanyType());

    log.info("업체 생성 검증 완료 - 업체명: {}, 타입: {}",
        company.getCompanyName(), company.getCompanyType());

    return company;
  }

  private void validateCompanyName(String companyName) {
    if (companyName == null || companyName.trim().length() < 2) {
      throw new IllegalArgumentException("업체명은 최소 2자 이상이어야 합니다.");
    }
    // 추가 검증 로직...
  }

  private void validateCompanyType(CompanyType companyType) {
    if (companyType == null) {
      throw new IllegalArgumentException("업체 타입은 필수입니다.");
    }
    // 추가 검증 로직...
  }
}