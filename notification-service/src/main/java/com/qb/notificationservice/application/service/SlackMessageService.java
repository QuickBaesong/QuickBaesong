package com.qb.notificationservice.application.service;

import com.qb.notificationservice.application.dto.SlackMessageCreateRequest;
import com.qb.notificationservice.application.dto.SlackMessageResponse;
import com.qb.notificationservice.domain.entity.SlackMessage;
import com.qb.notificationservice.domain.enums.DeliveryStatus;
import com.qb.notificationservice.domain.service.SlackMessageDomainService;
import com.qb.notificationservice.infrastructure.slack.SlackClient;
import com.qb.notificationservice.infrastructure.slack.SlackClient.SlackResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SlackMessageService {

  private final SlackMessageDomainService slackMessageDomainService;
  private final SlackClient slackClient;

  /**
   * 슬랙 메시지 생성 및 전송
   */
  public SlackMessageResponse createSlackMessage(SlackMessageCreateRequest request, UUID createdBy) {
    log.info("슬랙 메시지 생성 시작: 사용자ID={}, 타입={}, 우선순위={}",
        request.getUserId(), request.getMessageType(), request.getPriority());

    try {
      // 1. 메시지 생성 (DB 저장)
      SlackMessage slackMessage = slackMessageDomainService.createSlackMessage(
          request.getUserId(),
          request.getMessage(),
          request.getMessageType(),
          request.getPriorityOrDefault(),
          request.getSlackChannelOrDefault(),
          request.getOrderId(),
          request.getRecipientTypeOrDefault()
      );

      log.debug("슬랙 메시지 DB 저장 완료: ID={}", slackMessage.getSlackMessageId());

      // 2. 실제 슬랙으로 메시지 전송
      SlackResponse slackResponse = sendToSlack(slackMessage);

      // 3. 전송 결과에 따른 상태 업데이트
      SlackMessage updatedMessage = updateMessageAfterSend(slackMessage, slackResponse);

      // 4. 응답 DTO 변환
      SlackMessageResponse response = SlackMessageResponse.fromForCreation(updatedMessage);

      log.info("슬랙 메시지 생성 완료: ID={}, 전송상태={}",
          updatedMessage.getSlackMessageId(), updatedMessage.getDeliveryStatus());

      return response;

    } catch (Exception e) {
      log.error("슬랙 메시지 생성 실패: 사용자ID={}, 오류={}", request.getUserId(), e.getMessage(), e);
      throw new RuntimeException("슬랙 메시지 생성 중 오류가 발생했습니다", e);
    }
  }

  /**
   * 실제 슬랙으로 메시지 전송
   */
  private SlackResponse sendToSlack(SlackMessage slackMessage) {
    log.debug("슬랙 API 호출 시작: 메시지ID={}", slackMessage.getSlackMessageId());

    try {
      // 슬랙 클라이언트를 통해 메시지 전송
      SlackResponse response = slackClient.sendMessage(
          slackMessage.getSlackChannel(),
          buildSlackMessageContent(slackMessage),
          slackMessage.getMessageType().name(),
          slackMessage.getPriority().name()
      );

      if (response.isSuccess()) {
        log.info("슬랙 메시지 전송 성공: 메시지ID={}, 타임스탬프={}",
            slackMessage.getSlackMessageId(), response.getTimestamp());
      } else {
        log.error("슬랙 메시지 전송 실패: 메시지ID={}, 에러={}",
            slackMessage.getSlackMessageId(), response.getErrorMessage());
      }

      return response;

    } catch (Exception e) {
      log.error("슬랙 API 호출 예외: 메시지ID={}, 오류={}",
          slackMessage.getSlackMessageId(), e.getMessage(), e);

      return SlackResponse.failure("SLACK_API_ERROR", e.getMessage());
    }
  }

  /**
   * 슬랙 전송용 메시지 내용 구성
   */
  private String buildSlackMessageContent(SlackMessage slackMessage) {
    StringBuilder content = new StringBuilder();

    // 우선순위에 따른 이모지 추가
    String priorityEmoji = getPriorityEmoji(slackMessage.getPriority().name());
    content.append(priorityEmoji).append(" ");

    // 메시지 타입 표시
    content.append("[").append(slackMessage.getMessageType().getDescription()).append("] ");

    // 실제 메시지 내용
    content.append(slackMessage.getMessage());

    // 주문 ID가 있으면 추가
    if (slackMessage.getOrderId() != null) {
      content.append("\n📦 주문번호: ").append(slackMessage.getOrderId());
    }

    // 시간 정보
    content.append("\n🕐 전송시간: ").append(slackMessage.getSentAt());

    return content.toString();
  }

  /**
   * 우선순위별 이모지 반환
   */
  private String getPriorityEmoji(String priority) {
    return switch (priority) {
      case "URGENT" -> "🚨";
      case "HIGH" -> "⚡";
      case "NORMAL" -> "📢";
      case "LOW" -> "💬";
      default -> "📢";
    };
  }

  /**
   * 전송 후 메시지 상태 업데이트
   */
  private SlackMessage updateMessageAfterSend(SlackMessage slackMessage, SlackResponse slackResponse) {
    if (slackResponse.isSuccess()) {
      // 성공 시: 타임스탬프 설정 및 전송 완료 상태로 변경
      return slackMessageDomainService.setSlackMessageTs(
          slackMessage.getSlackMessageId(),
          slackResponse.getTimestamp()
      );
    } else {
      // 실패 시: 실패 상태로 변경
      return slackMessageDomainService.updateDeliveryStatus(
          slackMessage.getSlackMessageId(),
          DeliveryStatus.FAILED
      );
    }
  }

  /**
   * 메시지 생성 요청 검증
   */
  private void validateCreateRequest(SlackMessageCreateRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("슬랙 메시지 생성 요청은 필수입니다");
    }

    // 추가 비즈니스 규칙 검증
    if (request.isUrgentMessage() && request.getSlackChannelOrDefault().equals("#일반")) {
      log.warn("긴급 메시지가 일반 채널로 전송됨: {}", request.getMessage());
    }

    // 배송 관련 메시지는 주문 ID가 권장됨
    if (request.isDeliveryRelated() && !request.hasOrderId()) {
      log.warn("배송 관련 메시지에 주문 ID가 없음: {}", request.getMessage());
    }
  }
}