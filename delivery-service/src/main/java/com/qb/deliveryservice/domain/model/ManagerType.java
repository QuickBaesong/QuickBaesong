package com.qb.deliveryservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ManagerType {
    HUB("허브 배송 담당자"),
    COMPANY("업체 배송 담당자");

    private final String description;
}
