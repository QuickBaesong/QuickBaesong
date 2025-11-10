package com.qb.companyservice.application.dto;

import com.qb.companyservice.domain.entity.CompanyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class CompanyCreateRequest {

  @NotNull(message = "허브 ID는 필수입니다.")
  private UUID hubId;

  @NotBlank(message = "업체명은 필수입니다.")
  @Size(max = 20, message = "업체명은 20자를 초과할 수 없습니다.")
  private String companyName;

  @NotNull(message = "업체 타입은 필수입니다.")
  private CompanyType companyType;

  @NotBlank(message = "업체 주소는 필수입니다.")
  @Size(max = 50, message = "업체 주소는 50자를 초과할 수 없습니다.")
  private String companyAddress;

  @Builder
  public CompanyCreateRequest(UUID hubId, String companyName, CompanyType companyType, String companyAddress) {
    this.hubId = hubId;
    this.companyName = companyName;
    this.companyType = companyType;
    this.companyAddress = companyAddress;
  }
}