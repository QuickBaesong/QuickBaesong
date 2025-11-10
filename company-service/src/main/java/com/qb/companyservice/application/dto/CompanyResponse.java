package com.qb.companyservice.application.dto;

import com.qb.companyservice.domain.entity.Company;
import com.qb.companyservice.domain.entity.CompanyType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class CompanyResponse {

  private UUID companyId;
  private UUID hubId;
  private String companyName;
  private CompanyType companyType;
  private String companyAddress;
  private LocalDateTime createdAt;
  private String createdBy;
  private LocalDateTime updatedAt;
  private String updatedBy;
  private LocalDateTime deletedAt;
  private String deletedBy;

  public static CompanyResponse from(Company company) {
    return CompanyResponse.builder()
        .companyId(company.getCompanyId())
        .hubId(company.getHubId())
        .companyName(company.getCompanyName())
        .companyType(company.getCompanyType())
        .companyAddress(company.getCompanyAddress())
        .createdAt(company.getCreatedAt())
        .createdBy(company.getCreatedBy())
        .updatedAt(company.getUpdatedAt())
        .updatedBy(company.getUpdatedBy())
        .deletedAt(company.getDeletedAt())
        .deletedBy(company.getDeletedBy())
        .build();
  }
}