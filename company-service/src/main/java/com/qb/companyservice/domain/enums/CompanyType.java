package com.qb.companyservice.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CompanyType {
  SENDER("생산업체"),
  RECEIVER("수령업체");

  private final String description;
}