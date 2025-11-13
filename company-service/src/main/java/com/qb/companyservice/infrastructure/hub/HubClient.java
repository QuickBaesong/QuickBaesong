package com.qb.companyservice.infrastructure.hub;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * 허브 클라이언트 인터페이스
 * 허브 존재 여부 검증 등을 담당
 */
public interface HubClient {

  /**
   * 허브 존재 여부 확인
   *
   * @param hubId 허브 ID
   * @return 허브 존재 여부 응답
   */
  HubValidationResponse validateHub(UUID hubId);

  /**
   * 허브 검증 응답 DTO
   */
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class HubValidationResponse {
    private boolean exists;
    private String hubName;
    private String message;
    private String errorCode;

    public static HubValidationResponse success(String hubName) {
      return HubValidationResponse.builder()
          .exists(true)
          .hubName(hubName)
          .message("허브가 존재합니다")
          .build();
    }

    public static HubValidationResponse notFound(UUID hubId) {
      return HubValidationResponse.builder()
          .exists(false)
          .message("허브를 찾을 수 없습니다: " + hubId)
          .errorCode("HUB_NOT_FOUND")
          .build();
    }

    public static HubValidationResponse error(String errorMessage) {
      return HubValidationResponse.builder()
          .exists(false)
          .message("허브 검증 중 오류 발생: " + errorMessage)
          .errorCode("HUB_VALIDATION_ERROR")
          .build();
    }
  }
}