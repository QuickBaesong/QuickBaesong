package com.qb.companyservice.application.service;

import com.qb.companyservice.application.dto.CompanyCreateRequest;
import com.qb.companyservice.application.dto.CompanyResponse;
import com.qb.companyservice.domain.entity.Company;
import com.qb.companyservice.domain.service.CompanyDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {

  private final CompanyDomainService companyDomainService;

  @Transactional
  public CompanyResponse createCompany(CompanyCreateRequest request, UUID userId) {
    log.info("업체 생성 서비스 시작: 업체명={}", request.getCompanyName());

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
}