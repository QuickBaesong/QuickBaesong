package com.qb.notificationservice.domain.enums;

import lombok.Getter;

@Getter
public enum MessageType {
  DELIVERY_NOTIFICATION("배송 알림"),
  ORDER_CONFIRMATION("주문 확인"),
  SYSTEM_ALERT("시스템 알림"),
  URGENT_NOTICE("긴급 공지"),
  GENERAL("일반 메시지");

  private final String description;

  MessageType(String description) {
    this.description = description;
  }
}