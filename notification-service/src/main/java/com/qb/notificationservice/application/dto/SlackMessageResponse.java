package com.qb.notificationservice.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.qb.notificationservice.domain.entity.SlackMessage;
import com.qb.notificationservice.domain.enums.DeliveryStatus;
import com.qb.notificationservice.domain.enums.MessageType;
import com.qb.notificationservice.domain.enums.Priority;
import com.qb.notificationservice.domain.enums.RecipientType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlackMessageResponse {

  private String slackMessageId;
  private String userId;
  private String message;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime sentAt;

  private String messageType;
  private String priority;
  private String orderId;
  private String deliveryStatus;
  private String slackChannel;
  private String slackMessageTs;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime readAt;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime repliedAt;

  private String replyMessage;
  private String recipientType;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime createdAt;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime updatedAt;

  private String createdBy;
  private String updatedBy;

  /**
   * SlackMessage 엔티티로부터 DTO 생성
   */
  public static SlackMessageResponse from(SlackMessage slackMessage) {
    return SlackMessageResponse.builder()
        .slackMessageId(slackMessage.getSlackMessageId().toString())
        .userId(slackMessage.getUserId().toString())
        .message(slackMessage.getMessage())
        .sentAt(slackMessage.getSentAt())
        .messageType(slackMessage.getMessageType().name())
        .priority(slackMessage.getPriority().name())
        .orderId(slackMessage.getOrderId() != null ? slackMessage.getOrderId().toString() : null)
        .deliveryStatus(slackMessage.getDeliveryStatus().name())
        .slackChannel(slackMessage.getSlackChannel())
        .slackMessageTs(slackMessage.getSlackMessageTs())
        .readAt(slackMessage.getReadAt())
        .repliedAt(slackMessage.getRepliedAt())
        .replyMessage(slackMessage.getReplyMessage())
        .recipientType(slackMessage.getRecipientType().name())
        .createdAt(slackMessage.getCreatedAt())
        .updatedAt(slackMessage.getUpdatedAt())
        .createdBy(slackMessage.getCreatedBy())
        .updatedBy(slackMessage.getUpdatedBy())
        .build();
  }

  /**
   * 간단한 응답용 (생성 API 전용)
   */
  public static SlackMessageResponse fromForCreation(SlackMessage slackMessage) {
    return SlackMessageResponse.builder()
        .slackMessageId(slackMessage.getSlackMessageId().toString())
        .userId(slackMessage.getUserId().toString())
        .message(slackMessage.getMessage())
        .sentAt(slackMessage.getSentAt())
        .messageType(slackMessage.getMessageType().name())
        .priority(slackMessage.getPriority().name())
        .deliveryStatus(slackMessage.getDeliveryStatus().name())
        .slackChannel(slackMessage.getSlackChannel())
        .recipientType(slackMessage.getRecipientType().name())
        .createdAt(slackMessage.getCreatedAt())
        .createdBy(slackMessage.getCreatedBy())
        .build();
  }

  /**
   * 상세 조회용 (모든 정보 포함)
   */
  public static SlackMessageResponse fromForDetail(SlackMessage slackMessage) {
    return SlackMessageResponse.from(slackMessage);
  }

  // Helper 메서드들

  /**
   * 메시지가 읽혔는지 확인
   */
  public boolean isRead() {
    return this.readAt != null;
  }

  /**
   * 응답이 있는지 확인
   */
  public boolean hasReply() {
    return this.replyMessage != null && !this.replyMessage.trim().isEmpty();
  }

  /**
   * 전송 완료 상태인지 확인
   */
  public boolean isSent() {
    return DeliveryStatus.SENT.name().equals(this.deliveryStatus) ||
        DeliveryStatus.DELIVERED.name().equals(this.deliveryStatus) ||
        DeliveryStatus.READ.name().equals(this.deliveryStatus);
  }

  /**
   * 긴급 메시지인지 확인
   */
  public boolean isUrgent() {
    return Priority.URGENT.name().equals(this.priority);
  }

  /**
   * 배송 관련 메시지인지 확인
   */
  public boolean isDeliveryNotification() {
    return MessageType.DELIVERY_NOTIFICATION.name().equals(this.messageType);
  }

  /**
   * 주문 연관 메시지인지 확인
   */
  public boolean hasOrderId() {
    return this.orderId != null && !this.orderId.trim().isEmpty();
  }

  /**
   * 메시지 요약 정보 (로깅용)
   */
  public String getSummary() {
    return String.format("SlackMessage[id=%s, type=%s, priority=%s, status=%s]",
        slackMessageId, messageType, priority, deliveryStatus);
  }
}