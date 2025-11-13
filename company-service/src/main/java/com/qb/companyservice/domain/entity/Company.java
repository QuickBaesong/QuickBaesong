package com.qb.companyservice.domain.entity;

import com.qb.common.entity.BaseEntity;
import com.qb.companyservice.domain.enums.CompanyType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "p_company", schema = "company_schema")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
public class Company extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "company_id")
  private UUID companyId;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @Column(name = "hub_id", nullable = false)
  private UUID hubId;

  @Column(name = "company_name", nullable = false, length = 100)
  private String companyName;

  @Enumerated(EnumType.STRING)
  @Column(name = "company_type", nullable = false, length = 20)
  private CompanyType companyType;

  @Column(name = "company_address", nullable = false, length = 255)
  private String companyAddress;

  @Builder
  private Company(UUID userId, UUID hubId, String companyName,
      CompanyType companyType, String companyAddress) {
    this.userId = userId;
    this.hubId = hubId;
    this.companyName = companyName;
    this.companyType = companyType;
    this.companyAddress = companyAddress;
  }

  // 정적 팩토리 메서드
  public static Company create(UUID userId, UUID hubId, String companyName,
      CompanyType companyType, String companyAddress) {
    return Company.builder()
        .userId(userId)
        .hubId(hubId)
        .companyName(companyName)
        .companyType(companyType)
        .companyAddress(companyAddress)
        .build();
  }

  // 업체 정보 수정 메서드
  public void updateCompanyInfo(String companyName, CompanyType companyType, String companyAddress) {
    if (companyName != null && !companyName.trim().isEmpty()) {
      this.companyName = companyName;
    }
    if (companyType != null) {
      this.companyType = companyType;
    }
    if (companyAddress != null && !companyAddress.trim().isEmpty()) {
      this.companyAddress = companyAddress;
    }
  }

  // delete() 메서드 제거 - CompanyDomainService에서 처리
}