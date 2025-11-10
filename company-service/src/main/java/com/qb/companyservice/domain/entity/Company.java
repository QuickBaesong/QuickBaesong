package com.qb.companyservice.domain.entity;

import com.qb.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

/**
 * 업체 엔티티
 * 물류 시스템에서 관리하는 업체 정보를 나타냅니다.
 */
@Entity
@Table(name = "p_company")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company extends BaseEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "company_id")
  private UUID companyId;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @Column(name = "hub_id", nullable = false)
  private UUID hubId;

  @Column(name = "company_name", nullable = false, length = 20)
  private String companyName;

  @Enumerated(EnumType.STRING)
  @Column(name = "company_type", nullable = false, length = 20)
  private CompanyType companyType;

  @Column(name = "company_address", nullable = false, length = 50)
  private String companyAddress;

  @Builder
  public Company(UUID userId, UUID hubId, String companyName,
      CompanyType companyType, String companyAddress) {
    this.userId = userId;
    this.hubId = hubId;
    this.companyName = companyName;
    this.companyType = companyType;
    this.companyAddress = companyAddress;
  }

  /**
   * 업체 정보 수정
   */
  public void updateCompanyInfo(String companyName, String companyAddress) {
    if (companyName != null && !companyName.trim().isEmpty()) {
      this.companyName = companyName;
    }
    if (companyAddress != null && !companyAddress.trim().isEmpty()) {
      this.companyAddress = companyAddress;
    }
  }

  /**
   * 특정 허브에 소속된 업체인지 확인
   */
  public boolean belongsToHub(UUID hubId) {
    return this.hubId.equals(hubId);
  }

  /**
   * 생산업체인지 확인
   */
  public boolean isSender() {
    return CompanyType.SENDER.equals(this.companyType);
  }

  /**
   * 수령업체인지 확인
   */
  public boolean isReceiver() {
    return CompanyType.RECEIVER.equals(this.companyType);
  }
}