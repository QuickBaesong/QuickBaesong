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

  /**
   * 업체 생성 (피드백 반영: DTO에서 Entity 생성)
   * @param request 업체 생성 요청
   * @param userId 사용자 ID
   * @return 생성된 업체 정보
   */
  @Transactional
  public CompanyResponse createCompany(CompanyCreateRequest request, UUID userId) {
    log.info("업체 생성 요청 시작: 업체명={}, 타입={}, 사용자ID={}",
        request.getCompanyName(), request.getCompanyType(), userId);

    try {
      // DTO에서 Entity 생성 (피드백 반영)
      Company company = request.toEntity(userId);

      log.debug("유효성 검증된 사용자 ID: 원본={}, 최종={}", userId, company.getUserId());

      // 도메인 서비스를 통한 저장
      Company savedCompany = companyDomainService.save(company);

      // Entity에서 Response DTO로 변환
      CompanyResponse response = CompanyResponse.from(savedCompany);

      log.info("업체 생성 완료: ID={}, 이름={}",
          savedCompany.getCompanyId(), savedCompany.getCompanyName());

      return response;

    } catch (IllegalArgumentException e) {
      log.error("업체 생성 실패 - 유효성 검증 오류: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error("업체 생성 실패 - 시스템 오류", e);
      throw new RuntimeException("업체 생성 중 오류가 발생했습니다", e);
    }
  }
}