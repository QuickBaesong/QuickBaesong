package com.qb.companyservice.application.dto;

import com.qb.companyservice.domain.entity.Company;
import com.qb.companyservice.domain.enums.CompanyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class CompanyCreateRequest {

  @NotNull(message = "허브 ID는 필수입니다.")
  private UUID hubId;

  @NotBlank(message = "업체명은 필수입니다.")
  @Size(max = 20, message = "업체명은 20자 이하여야 합니다.")
  private String companyName;

  @NotNull(message = "업체 타입은 필수입니다.")
  private CompanyType companyType;

  @NotBlank(message = "업체 주소는 필수입니다.")
  @Size(max = 50, message = "업체 주소는 50자 이하여야 합니다.")
  private String companyAddress;

  public CompanyCreateRequest(UUID hubId, String companyName,
      CompanyType companyType, String companyAddress) {
    this.hubId = hubId;
    this.companyName = companyName;
    this.companyType = companyType;
    this.companyAddress = companyAddress;
  }

  // DTO에서 Entity 생성 로직 (피드백 반영)
  public Company toEntity(UUID userId) {
    // userId 유효성 검증 후 처리 (피드백 반영)
    UUID validUserId = validateAndGetUserId(userId);

    return Company.create(
        validUserId,
        this.hubId,
        this.companyName,
        this.companyType,
        this.companyAddress
    );
  }

  // userId 유효성 검증 로직 (피드백 반영)
  private UUID validateAndGetUserId(UUID userId) {
    if (userId == null) {
      return UUID.randomUUID();
    }

    try {
      // UUID 형식 검증
      return UUID.fromString(userId.toString());
    } catch (IllegalArgumentException e) {
      // 유효하지 않은 UUID일 시 랜덤 UUID 생성 (피드백 반영)
      return UUID.randomUUID();
    }
  }
}