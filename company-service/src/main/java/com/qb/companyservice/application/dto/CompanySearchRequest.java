package com.qb.companyservice.application.dto;

import com.qb.companyservice.domain.enums.CompanyType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanySearchRequest {

  @Size(max = 100, message = "업체명은 100자 이하여야 합니다")
  private String companyName;

  private CompanyType companyType;

  private UUID hubId;

  @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다")
  @Builder.Default
  private int page = 0;

  @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다")
  @Builder.Default
  private int size = 10;

  @Builder.Default
  private String sortBy = "createdAt";

  @Builder.Default
  private String sortDir = "desc";

  /**
   * 업체명 검색 조건이 있는지 확인
   */
  public boolean hasCompanyName() {
    return this.companyName != null && !this.companyName.trim().isEmpty();
  }

  /**
   * 업체 타입 검색 조건이 있는지 확인
   */
  public boolean hasCompanyType() {
    return this.companyType != null;
  }

  /**
   * 허브 검색 조건이 있는지 확인
   */
  public boolean hasHubId() {
    return this.hubId != null;
  }

  /**
   * 검색 조건이 하나라도 있는지 확인
   */
  public boolean hasSearchConditions() {
    return hasCompanyName() || hasCompanyType() || hasHubId();
  }

  /**
   * 정제된 업체명 반환
   */
  public String getCleanCompanyName() {
    return hasCompanyName() ? this.companyName.trim() : null;
  }

  /**
   * 유효한 정렬 방향인지 확인 후 반환
   */
  public String getValidSortDir() {
    if ("asc".equalsIgnoreCase(this.sortDir) || "desc".equalsIgnoreCase(this.sortDir)) {
      return this.sortDir.toLowerCase();
    }
    return "desc"; // 기본값
  }

  /**
   * 유효한 정렬 필드인지 확인 후 반환
   */
  public String getValidSortBy() {
    // 허용된 정렬 필드 목록
    return switch (this.sortBy) {
      case "companyName", "companyType", "companyAddress", "hubId", "createdAt", "updatedAt" -> this.sortBy;
      default -> "createdAt"; // 기본값
    };
  }

  /**
   * 페이지 크기 제한 (최대 100개)
   */
  public int getLimitedSize() {
    return Math.min(this.size, 100);
  }
}