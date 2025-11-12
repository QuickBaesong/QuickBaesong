package com.qb.notificationservice.presentation.controller;

import com.qb.common.enums.SuccessCode;
import com.qb.common.response.ApiResponse;
import com.qb.notificationservice.application.dto.SlackMessageCreateRequest;
import com.qb.notificationservice.application.dto.SlackMessageResponse;
import com.qb.notificationservice.application.service.SlackMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/v1/slack/messages")
@RequiredArgsConstructor
public class SlackMessageController {

  private final SlackMessageService slackMessageService;

  /**
   * 슬랙 메시지 생성 (발송) API
   * URL: POST /v1/slack/messages
   * 권한: COMPANY_MANAGER, DELIVERY_MANAGER, HUB_MANAGER, MASTER
   */
  @PostMapping
  public ResponseEntity<ApiResponse<SlackMessageResponse>> createSlackMessage(
      @Valid @RequestBody SlackMessageCreateRequest request,
      @RequestHeader("X-User-Id") String userIdHeader) {

    log.info("슬랙 메시지 생성 API 호출: 수신자={}, 타입={}, 우선순위={}, 채널={}",
        request.getUserId(), request.getMessageType(),
        request.getPriority(), request.getSlackChannelOrDefault());

    try {
      // 헤더에서 사용자 ID 추출 및 검증
      UUID createdBy = parseAndValidateUserId(userIdHeader);
      log.debug("요청자 사용자 ID: {}", createdBy);

      // 슬랙 메시지 생성 및 전송
      SlackMessageResponse response = slackMessageService.createSlackMessage(request, createdBy);

      // API 응답 생성 (Common 모듈 활용)
      ApiResponse<SlackMessageResponse> apiResponse = ApiResponse.of(
          SuccessCode.CREATED, response);

      log.info("슬랙 메시지 생성 성공: 메시지ID={}, 전송상태={}",
          response.getSlackMessageId(), response.getDeliveryStatus());

      return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);

    } catch (IllegalArgumentException e) {
      log.error("슬랙 메시지 생성 실패 - 유효성 검증 오류: {}", e.getMessage());
      throw e; // @ControllerAdvice에서 처리
    } catch (Exception e) {
      log.error("슬랙 메시지 생성 실패 - 시스템 오류", e);
      throw new RuntimeException("슬랙 메시지 생성 중 오류가 발생했습니다", e);
    }
  }

  /**
   * 헤더에서 사용자 ID 파싱 및 검증
   * 팀 피드백 반영: 임시 처리보다 명확한 에러 처리
   */
  private UUID parseAndValidateUserId(String userIdHeader) {
    // 헤더 존재 여부 검증
    if (userIdHeader == null || userIdHeader.trim().isEmpty()) {
      log.warn("X-User-Id 헤더가 누락됨");
      throw new IllegalArgumentException("X-User-Id 헤더는 필수입니다.");
    }

    // UUID 형식 검증
    try {
      UUID userId = UUID.fromString(userIdHeader.trim());
      log.debug("사용자 ID 파싱 성공: {}", userId);
      return userId;
    } catch (IllegalArgumentException e) {
      log.warn("유효하지 않은 X-User-Id 헤더 형식: {}", userIdHeader);
      throw new IllegalArgumentException("X-User-Id는 유효한 UUID 형식이어야 합니다.", e);
    }
  }

  /**
   * 요청 본문 추가 검증 (Validation 어노테이션 외 비즈니스 규칙)
   */
  private void validateBusinessRules(SlackMessageCreateRequest request) {
    // 긴급 메시지 특별 검증
    if (request.isUrgentMessage()) {
      log.debug("긴급 메시지 검증: {}", request.getMessage());

      if (request.getMessage().length() < 10) {
        throw new IllegalArgumentException("긴급 메시지는 최소 10자 이상이어야 합니다.");
      }
    }

    // 배송 관련 메시지는 주문 ID 권장
    if (request.isDeliveryRelated() && !request.hasOrderId()) {
      log.warn("배송 관련 메시지에 주문 ID가 없음. 메시지: {}",
          request.getMessage().substring(0, Math.min(50, request.getMessage().length())));
      // 경고만 로깅하고 진행 (비즈니스 요구사항에 따라 조정)
    }

    // 시스템 알림은 특정 채널로만 발송
    if (request.getMessageType().name().equals("SYSTEM_ALERT")) {
      String channel = request.getSlackChannelOrDefault();
      if (!channel.equals("#시스템알림") && !channel.equals("#관리자")) {
        log.warn("시스템 알림이 일반 채널로 발송 요청됨: {}", channel);
        // 필요시 예외 처리
      }
    }
  }
}