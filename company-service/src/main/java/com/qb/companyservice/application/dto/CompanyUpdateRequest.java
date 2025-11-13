package com.qb.companyservice.application.dto;

import com.qb.companyservice.domain.enums.CompanyType;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyUpdateRequest {

  @Size(min = 1, max = 100, message = "업체명은 1자 이상 100자 이하여야 합니다")
  private String companyName;

  private CompanyType companyType;

  @Size(min = 1, max = 255, message = "업체 주소는 1자 이상 255자 이하여야 합니다")
  private String companyAddress;

  /**
   * 업체명 변경 여부 확인
   */
  public boolean hasCompanyName() {
    return this.companyName != null && !this.companyName.trim().isEmpty();
  }

  /**
   * 업체 타입 변경 여부 확인
   */
  public boolean hasCompanyType() {
    return this.companyType != null;
  }

  /**
   * 주소 변경 여부 확인
   */
  public boolean hasCompanyAddress() {
    return this.companyAddress != null && !this.companyAddress.trim().isEmpty();
  }

  /**
   * 변경사항 존재 여부 확인
   */
  public boolean hasAnyChanges() {
    return hasCompanyName() || hasCompanyType() || hasCompanyAddress();
  }

  /**
   * 정제된 업체명 반환
   */
  public String getCleanCompanyName() {
    return hasCompanyName() ? this.companyName.trim() : null;
  }

  /**
   * 정제된 주소 반환
   */
  public String getCleanCompanyAddress() {
    return hasCompanyAddress() ? this.companyAddress.trim() : null;
  }
}