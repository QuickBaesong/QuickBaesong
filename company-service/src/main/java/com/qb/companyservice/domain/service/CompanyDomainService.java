package com.qb.companyservice.domain.service;

import com.qb.companyservice.common.enums.CompanyErrorCode;
import com.qb.companyservice.common.exception.CompanyException;
import com.qb.companyservice.domain.entity.Company;
import com.qb.companyservice.domain.entity.CompanyType;
import com.qb.companyservice.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 업체 도메인 서비스
 * 업체 관련 핵심 비즈니스 로직을 처리합니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyDomainService {

  private final CompanyRepository companyRepository;

  /**
   * 업체 생성
   *
   * @param userId 사용자 ID
   * @param hubId 허브 ID
   * @param companyName 업체명
   * @param companyType 업체 타입
   * @param companyAddress 업체 주소
   * @return 생성된 업체 엔티티
   */
  public Company createCompany(UUID userId, UUID hubId, String companyName,
      CompanyType companyType, String companyAddress) {

    log.info("업체 도메인 서비스 - 업체 생성 시작: 업체명={}, 타입={}", companyName, companyType);

    // 1. 업체명 중복 체크
    validateCompanyNameUnique(hubId, companyName);

    // 2. 업체 엔티티 생성
    Company company = Company.builder()
        .userId(userId)
        .hubId(hubId)
        .companyName(companyName)
        .companyType(companyType)
        .companyAddress(companyAddress)
        .build();

    log.info("업체 도메인 서비스 - 업체 생성 완료: 업체명={}", companyName);

    return company;
  }

  /**
   * 같은 허브 내에서 업체명 중복 검증
   *
   * @param hubId 허브 ID
   * @param companyName 업체명
   * @throws CompanyException 중복된 업체명이 존재할 경우
   */
  private void validateCompanyNameUnique(UUID hubId, String companyName) {

    log.debug("업체명 중복 체크 - 허브ID: {}, 업체명: {}", hubId, companyName);

    boolean exists = companyRepository.existsByHubIdAndCompanyNameAndDeletedAtIsNull(hubId, companyName);

    if (exists) {
      log.warn("업체명 중복 발견 - 허브ID: {}, 업체명: {}", hubId, companyName);
      throw new CompanyException(CompanyErrorCode.COMPANY_NAME_ALREADY_EXISTS);
    }

    log.debug("업체명 중복 체크 통과 - 허브ID: {}, 업체명: {}", hubId, companyName);
  }
}