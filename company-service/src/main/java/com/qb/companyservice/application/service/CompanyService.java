package com.qb.companyservice.application.service;

import com.qb.companyservice.application.dto.CompanyCreateRequest;
import com.qb.companyservice.application.dto.CompanyResponse;
import com.qb.companyservice.domain.entity.CompanyType;
import com.qb.common.response.PageResponse;

import java.util.UUID;

/**
 * 업체 Application Service 인터페이스
 * 업체 관련 유스케이스를 정의합니다.
 */
public interface CompanyService {

  /**
   * 업체 생성
   */
  CompanyResponse createCompany(CompanyCreateRequest request, String userId);

  /**
   * 업체 단건 조회
   */
  CompanyResponse findById(UUID companyId);

  /**
   * 업체 목록 조회 (페이징, 검색)
   */
  PageResponse<CompanyResponse> findCompanies(String name, CompanyType type, UUID hubId,
      String sortBy, int page, int size);
}