package com.qb.companyservice.application.dto;

import com.qb.companyservice.domain.entity.Company;
import com.qb.companyservice.domain.entity.CompanyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyCreateRequest {

  @NotBlank(message = "업체명은 필수입니다.")
  private String companyName;

  @NotNull(message = "업체 타입은 필수입니다.")
  private CompanyType companyType;

  @NotNull(message = "허브 ID는 필수입니다.")
  private UUID hubId;

  private String companyAddress;

  /**
   * DTO에서 Entity로 변환하는 메서드
   * @param userId 생성자 UUID
   * @return Company Entity
   */
  public Company toEntity(UUID userId) {
    Company company = Company.builder()
        .hubId(this.hubId)
        .companyName(this.companyName)
        .companyType(this.companyType)
        .companyAddress(this.companyAddress != null && !this.companyAddress.trim().isEmpty()
            ? this.companyAddress : null)
        .build();

    // BaseEntity 필드 설정
    company.setCreatedBy(userId.toString());

    return company;
  }
}