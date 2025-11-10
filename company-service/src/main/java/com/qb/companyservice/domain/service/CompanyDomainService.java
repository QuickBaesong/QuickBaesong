package com.qb.companyservice.domain.service;

import com.qb.companyservice.domain.entity.Company;
import com.qb.companyservice.domain.entity.CompanyType;
import com.qb.companyservice.domain.repository.CompanyRepository;
import com.qb.common.exception.CustomException;
import com.qb.common.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 업체 도메인 서비스
 * 업체 관련 비즈니스 규칙과 도메인 로직을 담당합니다.
 */
@Service
@RequiredArgsConstructor
public class CompanyDomainService {

  private final CompanyRepository companyRepository;

  /**
   * 업체 생성 전 비즈니스 규칙 검증
   */
  public void validateCompanyCreation(UUID hubId, String companyName) {
    // 같은 허브 내 업체명 중복 체크
    if (companyRepository.existsByHubIdAndCompanyNameAndDeletedAtIsNull(hubId, companyName)) {
      throw new CustomException(ErrorCode.DUPLICATE_COMPANY_NAME);
    }
  }

  /**
   * 업체명 유효성 검증
   */
  public void validateCompanyName(String companyName) {
    if (companyName == null || companyName.trim().isEmpty()) {
      throw new CustomException(ErrorCode.INVALID_COMPANY_NAME);
    }
    if (companyName.length() > 20) {
      throw new CustomException(ErrorCode.COMPANY_NAME_TOO_LONG);
    }
  }

  /**
   * 업체 주소 유효성 검증
   */
  public void validateCompanyAddress(String companyAddress) {
    if (companyAddress == null || companyAddress.trim().isEmpty()) {
      throw new CustomException(ErrorCode.INVALID_COMPANY_ADDRESS);
    }
    if (companyAddress.length() > 50) {
      throw new CustomException(ErrorCode.COMPANY_ADDRESS_TOO_LONG);
    }
  }

  /**
   * 업체 생성 도메인 로직
   */
  public Company createCompany(UUID userId, UUID hubId, String companyName,
      CompanyType companyType, String companyAddress) {
    // 비즈니스 규칙 검증
    validateCompanyName(companyName);
    validateCompanyAddress(companyAddress);
    validateCompanyCreation(hubId, companyName);

    // 업체 생성
    return Company.builder()
        .userId(userId)
        .hubId(hubId)
        .companyName(companyName)
        .companyType(companyType)
        .companyAddress(companyAddress)
        .build();
  }
}