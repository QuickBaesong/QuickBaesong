package com.qb.hubservice.application.service;

import lombok.Getter;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
class Node {
 final UUID id;
 final BigDecimal distance;
 final Long duration;
}