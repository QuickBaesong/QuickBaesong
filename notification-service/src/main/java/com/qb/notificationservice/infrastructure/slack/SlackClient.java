package com.qb.notificationservice.infrastructure.slack;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 슬랙 클라이언트 인터페이스
 */
public interface SlackClient {

  /**
   * 슬랙 메시지 전송
   *
   * @param channel 채널명
   * @param message 메시지 내용
   * @param messageType 메시지 타입
   * @return 전송 결과
   */
  SlackResponse sendMessage(String channel, String message, String messageType);

  /**
   * 슬랙 메시지 전송 (우선순위 포함)
   *
   * @param channel 채널명
   * @param message 메시지 내용
   * @param messageType 메시지 타입
   * @param priority 우선순위
   * @return 전송 결과
   */
  SlackResponse sendMessage(String channel, String message, String messageType, String priority);

  /**
   * 슬랙 응답 DTO
   */
  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  class SlackResponse {
    private boolean success;
    private String timestamp;
    private String channel;
    private String message;
    private String errorCode;
    private String errorMessage;

    public static SlackResponse success(String timestamp, String channel) {
      return SlackResponse.builder()
          .success(true)
          .timestamp(timestamp)
          .channel(channel)
          .message("메시지가 성공적으로 전송되었습니다")
          .build();
    }

    public static SlackResponse failure(String errorCode, String errorMessage) {
      return SlackResponse.builder()
          .success(false)
          .errorCode(errorCode)
          .errorMessage(errorMessage)
          .message("메시지 전송에 실패했습니다")
          .build();
    }
  }
}