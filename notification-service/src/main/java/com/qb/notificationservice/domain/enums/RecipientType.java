package com.qb.notificationservice.domain.enums;

import lombok.Getter;

@Getter
public enum RecipientType {
  INDIVIDUAL("개인"),
  GROUP("그룹"),
  CHANNEL("채널"),
  BROADCAST("전체 방송");

  private final String description;

  RecipientType(String description) {
    this.description = description;
  }
}