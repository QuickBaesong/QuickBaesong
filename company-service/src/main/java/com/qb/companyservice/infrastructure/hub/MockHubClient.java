package com.qb.companyservice.infrastructure.hub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

/**
 * Mock 허브 클라이언트 (로컬/테스트 환경용)
 */
@Component
@Profile({"local", "test"})
@Slf4j
public class MockHubClient implements HubClient {

  // Mock 허브 데이터
  private static final Set<String> MOCK_HUB_IDS = Set.of(
      "a4b2c3d4-e5f6-7890-1234-567890abcdef", // 서울특별시 센터
      "b5c3d4e5-f6g7-8901-2345-678901bcdefg", // 경기 남부 센터
      "c6d4e5f6-g7h8-9012-3456-789012cdefgh", // 경기 북부 센터
      "d7e5f6g7-h8i9-0123-4567-890123defghi", // 부산광역시 센터
      "e8f6g7h8-i9j0-1234-5678-901234efghij"  // 대구광역시 센터
  );

  @Override
  public HubValidationResponse validateHub(UUID hubId) {
    log.info("🏢 ========== Mock Hub 검증 ==========");
    log.info("📍 허브 ID: {}", hubId);

    try {
      boolean exists = MOCK_HUB_IDS.contains(hubId.toString());

      if (exists) {
        String hubName = getMockHubName(hubId.toString());
        log.info("✅ 허브 존재 확인: {}", hubName);
        log.info("🏢 ===================================");

        return HubValidationResponse.success(hubName);
      } else {
        log.warn("❌ 허브 존재하지 않음: {}", hubId);
        log.info("💡 등록된 Mock 허브 목록:");
        MOCK_HUB_IDS.forEach(id -> log.info("  - {}", id));
        log.info("🏢 ===================================");

        return HubValidationResponse.notFound(hubId);
      }

    } catch (Exception e) {
      log.error("🚨 허브 검증 중 오류 발생: {}", e.getMessage(), e);
      log.info("🏢 ===================================");

      return HubValidationResponse.error(e.getMessage());
    }
  }

  private String getMockHubName(String hubId) {
    return switch (hubId) {
      case "a4b2c3d4-e5f6-7890-1234-567890abcdef" -> "서울특별시 센터";
      case "b5c3d4e5-f6g7-8901-2345-678901bcdefg" -> "경기 남부 센터";
      case "c6d4e5f6-g7h8-9012-3456-789012cdefgh" -> "경기 북부 센터";
      case "d7e5f6g7-h8i9-0123-4567-890123defghi" -> "부산광역시 센터";
      case "e8f6g7h8-i9j0-1234-5678-901234efghij" -> "대구광역시 센터";
      default -> "Unknown Hub";
    };
  }
}