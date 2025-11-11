package com.qb.companyservice.domain.entity;

import com.qb.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "companies")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Company extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(columnDefinition = "BINARY(16)")
  private UUID companyId;

  @Column(nullable = false)
  private UUID hubId;

  @Column(nullable = false, length = 100)
  private String companyName;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CompanyType companyType;

  @Column(length = 255)
  private String companyAddress;

  /**
   * 업체 정보 수정
   * @param companyName 업체명
   * @param companyAddress 업체 주소
   */
  public void updateCompanyInfo(String companyName, String companyAddress) {
    if (companyName != null && !companyName.trim().isEmpty()) {
      this.companyName = companyName;
    }
    if (companyAddress != null && !companyAddress.trim().isEmpty()) {
      this.companyAddress = companyAddress;
    }
  }
}