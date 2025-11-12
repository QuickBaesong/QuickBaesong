package com.qb.notificationservice.domain.enums;

import lombok.Getter;

@Getter
public enum DeliveryStatus {
  PENDING("전송 대기"),
  SENT("전송 완료"),
  DELIVERED("수신 확인"),
  READ("읽음 확인"),
  FAILED("전송 실패"),
  CANCELLED("전송 취소");

  private final String description;

  DeliveryStatus(String description) {
    this.description = description;
  }

  // 전송 완료 상태인지 확인
  public boolean isSent() {
    return this == SENT || this == DELIVERED || this == READ;
  }

  // 실패 상태인지 확인
  public boolean isFailed() {
    return this == FAILED;
  }

  // 읽음 상태인지 확인
  public boolean isRead() {
    return this == READ;
  }
}