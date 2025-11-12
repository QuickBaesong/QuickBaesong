package com.qb.notificationservice.domain.enums;

import lombok.Getter;

@Getter
public enum Priority {
  LOW("낮음", 1),
  NORMAL("보통", 2),
  HIGH("높음", 3),
  URGENT("긴급", 4);

  private final String description;
  private final int level;

  Priority(String description, int level) {
    this.description = description;
    this.level = level;
  }
}