package com.qb.notificationservice.domain.entity;

import com.qb.common.entity.BaseEntity;
import com.qb.notificationservice.domain.enums.DeliveryStatus;
import com.qb.notificationservice.domain.enums.MessageType;
import com.qb.notificationservice.domain.enums.Priority;
import com.qb.notificationservice.domain.enums.RecipientType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_slack_messages", schema = "notification_schema")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlackMessage extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "slack_message_id")
  private UUID slackMessageId;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @Column(name = "message", nullable = false, columnDefinition = "TEXT")
  private String message;

  @Column(name = "sent_at", nullable = false)
  private LocalDateTime sentAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "message_type", length = 50)
  private MessageType messageType;

  @Enumerated(EnumType.STRING)
  @Column(name = "priority", length = 20)
  private Priority priority;

  @Column(name = "order_id")
  private UUID orderId;

  @Enumerated(EnumType.STRING)
  @Column(name = "delivery_status", length = 20)
  private DeliveryStatus deliveryStatus;

  @Column(name = "slack_channel", length = 100)
  private String slackChannel;

  @Column(name = "slack_message_ts", length = 50)
  private String slackMessageTs;

  @Column(name = "read_at")
  private LocalDateTime readAt;

  @Column(name = "replied_at")
  private LocalDateTime repliedAt;

  @Column(name = "reply_message", columnDefinition = "TEXT")
  private String replyMessage;

  @Enumerated(EnumType.STRING)
  @Column(name = "recipient_type")
  private RecipientType recipientType;

  /**
   * 메시지 읽음 처리
   */
  public void markAsRead(LocalDateTime readTime) {
    this.readAt = readTime;
    this.deliveryStatus = DeliveryStatus.READ;
  }

  /**
   * 응답 메시지 설정
   */
  public void setReply(String replyMessage, LocalDateTime repliedTime) {
    this.replyMessage = replyMessage;
    this.repliedAt = repliedTime;
  }

  /**
   * 전송 상태 업데이트
   */
  public void updateDeliveryStatus(DeliveryStatus newStatus) {
    this.deliveryStatus = newStatus;
  }

  /**
   * 슬랙 메시지 타임스탬프 설정 (실제 전송 후)
   */
  public void setSlackMessageTs(String messageTs) {
    this.slackMessageTs = messageTs;
  }

  /**
   * 전송 시간 업데이트
   */
  public void updateSentAt(LocalDateTime sentTime) {
    this.sentAt = sentTime;
    this.deliveryStatus = DeliveryStatus.SENT;
  }
}