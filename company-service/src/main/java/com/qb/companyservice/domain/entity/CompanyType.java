package com.qb.companyservice.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 업체 타입 Enum
 * - SENDER: 생산업체 (물건을 보내는 업체)
 * - RECEIVER: 수령업체 (물건을 받는 업체)
 */
@Getter
@RequiredArgsConstructor
public enum CompanyType {
  SENDER("생산업체"),
  RECEIVER("수령업체");

  private final String description;
}