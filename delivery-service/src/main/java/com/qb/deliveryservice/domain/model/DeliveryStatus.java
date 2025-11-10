package com.qb.deliveryservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeliveryStatus {
    HUB_WAITING("허브 대기중"),
    HUB_MOVING("허브 이동중"),
    DESTINATION_ARRIVED("목적지 허브 도착"),
    COMPANY_DELIVERING("업체 이동중"),
    DELIVERY_COMPLETED("배송 완료");

    private final String description;

}
