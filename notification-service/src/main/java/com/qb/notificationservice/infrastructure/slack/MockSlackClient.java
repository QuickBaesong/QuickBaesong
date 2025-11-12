package com.qb.notificationservice.infrastructure.slack;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Mock 슬랙 클라이언트 (로컬/테스트 환경용)
 */
@Component
@Profile({"local", "test"})
@Slf4j
public class MockSlackClient implements SlackClient {

  @Override
  public SlackResponse sendMessage(String channel, String message, String messageType) {
    return sendMessage(channel, message, messageType, "NORMAL");
  }

  @Override
  public SlackResponse sendMessage(String channel, String message, String messageType, String priority) {
    log.info("🔥 ============= Mock Slack Message 발송 =============");
    log.info("📢 채널: {}", channel);
    log.info("💬 메시지: {}", message);
    log.info("🏷️ 타입: {}", messageType);
    log.info("⚡ 우선순위: {}", priority);
    log.info("🔥 ================================================");

    // Mock 타임스탬프 생성
    String mockTimestamp = "mock_ts_" + System.currentTimeMillis();

    return SlackResponse.success(mockTimestamp, channel);
  }
}