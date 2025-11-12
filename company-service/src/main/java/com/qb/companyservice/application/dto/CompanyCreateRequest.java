package com.qb.companyservice.application.dto;

import com.qb.companyservice.domain.enums.CompanyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyCreateRequest {

  @NotNull(message = "허브 ID는 필수입니다")
  private UUID hubId;

  @NotBlank(message = "업체명은 필수입니다")
  private String companyName;

  @NotNull(message = "업체 타입은 필수입니다")
  private CompanyType companyType;

  @NotBlank(message = "업체 주소는 필수입니다")
  private String companyAddress;
}