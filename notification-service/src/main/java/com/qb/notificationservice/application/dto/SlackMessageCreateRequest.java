package com.qb.notificationservice.application.dto;

import com.qb.notificationservice.domain.enums.MessageType;
import com.qb.notificationservice.domain.enums.Priority;
import com.qb.notificationservice.domain.enums.RecipientType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class SlackMessageCreateRequest {

  @NotNull(message = "수신자 사용자 ID는 필수입니다")
  private UUID userId;

  @NotBlank(message = "메시지 내용은 필수입니다")
  @Size(max = 4000, message = "메시지는 4000자를 초과할 수 없습니다")
  private String message;

  @NotNull(message = "메시지 타입은 필수입니다")
  private MessageType messageType;

  @Builder.Default
  private Priority priority = Priority.NORMAL;

  @Size(max = 100, message = "슬랙 채널명은 100자를 초과할 수 없습니다")
  private String slackChannel;

  private UUID orderId;

  @Builder.Default
  private RecipientType recipientType = RecipientType.INDIVIDUAL;

  /**
   * 기본 슬랙 채널 설정
   */
  public String getSlackChannelOrDefault() {
    return this.slackChannel != null ? this.slackChannel : "#알림";
  }

  /**
   * 우선순위 기본값 설정
   */
  public Priority getPriorityOrDefault() {
    return this.priority != null ? this.priority : Priority.NORMAL;
  }

  /**
   * 수신자 타입 기본값 설정
   */
  public RecipientType getRecipientTypeOrDefault() {
    return this.recipientType != null ? this.recipientType : RecipientType.INDIVIDUAL;
  }

  /**
   * 유효성 검증 헬퍼 메서드
   */
  public boolean hasOrderId() {
    return this.orderId != null;
  }

  /**
   * 긴급 메시지 여부 확인
   */
  public boolean isUrgentMessage() {
    return Priority.URGENT.equals(this.priority);
  }

  /**
   * 배송 관련 메시지 여부 확인
   */
  public boolean isDeliveryRelated() {
    return MessageType.DELIVERY_NOTIFICATION.equals(this.messageType);
  }
}