package com.qb.hubservice.application.service;

import lombok.Getter;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Edge { // default 접근 제한자 (같은 패키지 내에서만 접근 가능)
    final UUID destinationHubId;
    final BigDecimal distance;
    final Long duration;
}