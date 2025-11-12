package com.qb.notificationservice.domain.service;

import com.qb.notificationservice.domain.entity.SlackMessage;
import com.qb.notificationservice.domain.enums.DeliveryStatus;
import com.qb.notificationservice.domain.enums.MessageType;
import com.qb.notificationservice.domain.enums.Priority;
import com.qb.notificationservice.domain.enums.RecipientType;
import com.qb.notificationservice.domain.repository.SlackMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SlackMessageDomainService {

  private final SlackMessageRepository slackMessageRepository;

  /**
   * 슬랙 메시지 생성 및 저장
   */
  @Transactional
  public SlackMessage createSlackMessage(UUID userId, String message, MessageType messageType,
      Priority priority, String channel, UUID orderId,
      RecipientType recipientType) {

    log.debug("슬랙 메시지 생성 시작: 사용자ID={}, 메시지타입={}, 우선순위={}",
        userId, messageType, priority);

    // 비즈니스 규칙 검증
    validateMessageCreation(userId, message, messageType);

    // 슬랙 메시지 엔티티 생성
    SlackMessage slackMessage = SlackMessage.builder()
        .userId(userId)
        .message(message)
        .sentAt(LocalDateTime.now())
        .messageType(messageType)
        .priority(priority != null ? priority : Priority.NORMAL)
        .deliveryStatus(DeliveryStatus.PENDING)
        .slackChannel(channel)
        .orderId(orderId)
        .recipientType(recipientType != null ? recipientType : RecipientType.INDIVIDUAL)
        .build();

    // 저장
    SlackMessage savedMessage = slackMessageRepository.save(slackMessage);

    log.info("슬랙 메시지 생성 완료: ID={}, 사용자ID={}",
        savedMessage.getSlackMessageId(), savedMessage.getUserId());

    return savedMessage;
  }

  /**
   * 메시지 ID로 조회 (삭제되지 않은 메시지만)
   */
  public Optional<SlackMessage> findByIdAndNotDeleted(UUID messageId) {
    log.debug("슬랙 메시지 조회: ID={}", messageId);
    return slackMessageRepository.findByIdAndNotDeleted(messageId);
  }

  /**
   * 메시지 ID로 조회 (예외 발생 버전)
   */
  public SlackMessage getByIdAndNotDeleted(UUID messageId) {
    return findByIdAndNotDeleted(messageId)
        .orElseThrow(() -> {
          log.warn("슬랙 메시지를 찾을 수 없음: ID={}", messageId);
          return new IllegalArgumentException("슬랙 메시지를 찾을 수 없습니다: " + messageId);
        });
  }

  /**
   * 사용자별 메시지 목록 조회
   */
  public Page<SlackMessage> findMessagesByUserId(UUID userId, Pageable pageable) {
    log.debug("사용자별 슬랙 메시지 조회: 사용자ID={}, 페이지={}", userId, pageable.getPageNumber());
    return slackMessageRepository.findByUserIdAndNotDeleted(userId, pageable);
  }

  /**
   * 메시지 타입별 조회
   */
  public Page<SlackMessage> findMessagesByType(MessageType messageType, Pageable pageable) {
    log.debug("메시지 타입별 조회: 타입={}, 페이지={}", messageType, pageable.getPageNumber());
    return slackMessageRepository.findByMessageTypeAndNotDeleted(messageType, pageable);
  }

  /**
   * 우선순위별 조회
   */
  public Page<SlackMessage> findMessagesByPriority(Priority priority, Pageable pageable) {
    log.debug("우선순위별 조회: 우선순위={}, 페이지={}", priority, pageable.getPageNumber());
    return slackMessageRepository.findByPriorityAndNotDeleted(priority, pageable);
  }

  /**
   * 기간별 메시지 조회
   */
  public Page<SlackMessage> findMessagesByDateRange(LocalDateTime startDate, LocalDateTime endDate,
      Pageable pageable) {
    log.debug("기간별 메시지 조회: 시작일={}, 종료일={}", startDate, endDate);

    // 날짜 유효성 검증
    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("시작일은 종료일보다 이후일 수 없습니다");
    }

    return slackMessageRepository.findByDateRangeAndNotDeleted(startDate, endDate, pageable);
  }

  /**
   * 메시지 내용으로 검색
   */
  public Page<SlackMessage> searchByMessageContent(String keyword, Pageable pageable) {
    log.debug("메시지 내용 검색: 키워드={}", keyword);

    if (keyword == null || keyword.trim().isEmpty()) {
      throw new IllegalArgumentException("검색 키워드는 필수입니다");
    }

    return slackMessageRepository.searchByMessageContentAndNotDeleted(keyword.trim(), pageable);
  }

  /**
   * 주문 관련 메시지 조회
   */
  public List<SlackMessage> findMessagesByOrderId(UUID orderId) {
    log.debug("주문별 메시지 조회: 주문ID={}", orderId);
    return slackMessageRepository.findByOrderIdAndNotDeleted(orderId);
  }

  /**
   * 읽지 않은 메시지 조회
   */
  public List<SlackMessage> findUnreadMessages(UUID userId) {
    log.debug("읽지 않은 메시지 조회: 사용자ID={}", userId);
    return slackMessageRepository.findUnreadMessagesByUserId(userId);
  }

  /**
   * 전송 상태별 메시지 조회
   */
  public List<SlackMessage> findMessagesByDeliveryStatus(DeliveryStatus status) {
    log.debug("전송 상태별 조회: 상태={}", status);
    return slackMessageRepository.findByDeliveryStatusAndNotDeleted(status);
  }

  /**
   * 메시지 읽음 처리
   */
  @Transactional
  public SlackMessage markAsRead(UUID messageId, UUID userId) {
    log.debug("메시지 읽음 처리: 메시지ID={}, 사용자ID={}", messageId, userId);

    SlackMessage message = getByIdAndNotDeleted(messageId);

    // 사용자 권한 검증
    if (!message.getUserId().equals(userId)) {
      log.warn("메시지 읽음 권한 없음: 메시지ID={}, 요청사용자={}, 메시지사용자={}",
          messageId, userId, message.getUserId());
      throw new IllegalArgumentException("해당 메시지를 읽을 권한이 없습니다");
    }

    // 이미 읽은 메시지인지 확인
    if (message.getReadAt() != null) {
      log.debug("이미 읽은 메시지: ID={}", messageId);
      return message;
    }

    // 읽음 처리
    message.markAsRead(LocalDateTime.now());

    log.info("메시지 읽음 처리 완료: ID={}", messageId);
    return slackMessageRepository.save(message);
  }

  /**
   * 응답 메시지 설정
   */
  @Transactional
  public SlackMessage setReplyMessage(UUID messageId, String replyMessage, UUID userId) {
    log.debug("응답 메시지 설정: 메시지ID={}, 사용자ID={}", messageId, userId);

    SlackMessage message = getByIdAndNotDeleted(messageId);

    // 사용자 권한 검증
    if (!message.getUserId().equals(userId)) {
      throw new IllegalArgumentException("해당 메시지에 응답할 권한이 없습니다");
    }

    // 응답 메시지 검증
    validateReplyMessage(replyMessage);

    // 응답 설정
    message.setReply(replyMessage, LocalDateTime.now());

    log.info("응답 메시지 설정 완료: ID={}", messageId);
    return slackMessageRepository.save(message);
  }

  /**
   * 전송 상태 업데이트
   */
  @Transactional
  public SlackMessage updateDeliveryStatus(UUID messageId, DeliveryStatus newStatus) {
    log.debug("전송 상태 업데이트: 메시지ID={}, 새상태={}", messageId, newStatus);

    SlackMessage message = getByIdAndNotDeleted(messageId);
    message.updateDeliveryStatus(newStatus);

    log.info("전송 상태 업데이트 완료: ID={}, 상태={}", messageId, newStatus);
    return slackMessageRepository.save(message);
  }

  /**
   * 슬랙 메시지 타임스탬프 설정 (실제 전송 후)
   */
  @Transactional
  public SlackMessage setSlackMessageTs(UUID messageId, String messageTs) {
    log.debug("슬랙 타임스탬프 설정: 메시지ID={}, 타임스탬프={}", messageId, messageTs);

    SlackMessage message = getByIdAndNotDeleted(messageId);
    message.setSlackMessageTs(messageTs);
    message.updateDeliveryStatus(DeliveryStatus.SENT);

    log.info("슬랙 타임스탬프 설정 완료: ID={}", messageId);
    return slackMessageRepository.save(message);
  }

  /**
   * 메시지 논리적 삭제
   */
  @Transactional
  public void deleteMessage(UUID messageId, UUID deletedBy) {
    log.debug("메시지 삭제: 메시지ID={}, 삭제자={}", messageId, deletedBy);

    SlackMessage message = getByIdAndNotDeleted(messageId);

    // 논리적 삭제 처리 (BaseEntity의 delete 메서드 활용)
    message.softDelete(deletedBy.toString());

    slackMessageRepository.save(message);

    log.info("메시지 삭제 완료: ID={}, 삭제자={}", messageId, deletedBy);
  }

  /**
   * 메시지 생성 검증
   */
  private void validateMessageCreation(UUID userId, String message, MessageType messageType) {
    if (userId == null) {
      throw new IllegalArgumentException("사용자 ID는 필수입니다");
    }

    if (message == null || message.trim().isEmpty()) {
      throw new IllegalArgumentException("메시지 내용은 필수입니다");
    }

    if (message.length() > 4000) {
      throw new IllegalArgumentException("메시지는 4000자를 초과할 수 없습니다");
    }

    if (messageType == null) {
      throw new IllegalArgumentException("메시지 타입은 필수입니다");
    }
  }

  /**
   * 응답 메시지 검증
   */
  private void validateReplyMessage(String replyMessage) {
    if (replyMessage == null || replyMessage.trim().isEmpty()) {
      throw new IllegalArgumentException("응답 메시지는 필수입니다");
    }

    if (replyMessage.length() > 2000) {
      throw new IllegalArgumentException("응답 메시지는 2000자를 초과할 수 없습니다");
    }
  }
}