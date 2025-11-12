package com.qb.notificationservice.domain.repository;

import com.qb.notificationservice.domain.entity.SlackMessage;
import com.qb.notificationservice.domain.enums.DeliveryStatus;
import com.qb.notificationservice.domain.enums.MessageType;
import com.qb.notificationservice.domain.enums.Priority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SlackMessageRepository extends JpaRepository<SlackMessage, UUID> {

  /**
   * 사용자별 메시지 조회 (삭제되지 않은 메시지만)
   */
  @Query("SELECT sm FROM SlackMessage sm WHERE sm.userId = :userId AND sm.deletedAt IS NULL ORDER BY sm.sentAt DESC")
  Page<SlackMessage> findByUserIdAndNotDeleted(@Param("userId") UUID userId, Pageable pageable);

  /**
   * 전송 상태별 메시지 조회
   */
  @Query("SELECT sm FROM SlackMessage sm WHERE sm.deliveryStatus = :status AND sm.deletedAt IS NULL")
  List<SlackMessage> findByDeliveryStatusAndNotDeleted(@Param("status") DeliveryStatus status);

  /**
   * 메시지 타입별 조회
   */
  @Query("SELECT sm FROM SlackMessage sm WHERE sm.messageType = :messageType AND sm.deletedAt IS NULL ORDER BY sm.sentAt DESC")
  Page<SlackMessage> findByMessageTypeAndNotDeleted(@Param("messageType") MessageType messageType, Pageable pageable);

  /**
   * 우선순위별 조회
   */
  @Query("SELECT sm FROM SlackMessage sm WHERE sm.priority = :priority AND sm.deletedAt IS NULL ORDER BY sm.sentAt DESC")
  Page<SlackMessage> findByPriorityAndNotDeleted(@Param("priority") Priority priority, Pageable pageable);

  /**
   * 기간별 메시지 조회
   */
  @Query("SELECT sm FROM SlackMessage sm WHERE sm.sentAt BETWEEN :startDate AND :endDate AND sm.deletedAt IS NULL ORDER BY sm.sentAt DESC")
  Page<SlackMessage> findByDateRangeAndNotDeleted(
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate,
      Pageable pageable);

  /**
   * 메시지 내용으로 검색 (LIKE 검색)
   */
  @Query("SELECT sm FROM SlackMessage sm WHERE UPPER(sm.message) LIKE UPPER(CONCAT('%', :keyword, '%')) AND sm.deletedAt IS NULL ORDER BY sm.sentAt DESC")
  Page<SlackMessage> searchByMessageContentAndNotDeleted(@Param("keyword") String keyword, Pageable pageable);

  /**
   * 주문 ID로 관련 메시지 조회
   */
  @Query("SELECT sm FROM SlackMessage sm WHERE sm.orderId = :orderId AND sm.deletedAt IS NULL ORDER BY sm.sentAt DESC")
  List<SlackMessage> findByOrderIdAndNotDeleted(@Param("orderId") UUID orderId);

  /**
   * 읽지 않은 메시지 조회
   */
  @Query("SELECT sm FROM SlackMessage sm WHERE sm.userId = :userId AND sm.readAt IS NULL AND sm.deletedAt IS NULL ORDER BY sm.sentAt DESC")
  List<SlackMessage> findUnreadMessagesByUserId(@Param("userId") UUID userId);

  /**
   * ID로 삭제되지 않은 메시지 조회
   */
  @Query("SELECT sm FROM SlackMessage sm WHERE sm.slackMessageId = :messageId AND sm.deletedAt IS NULL")
  Optional<SlackMessage> findByIdAndNotDeleted(@Param("messageId") UUID messageId);
}