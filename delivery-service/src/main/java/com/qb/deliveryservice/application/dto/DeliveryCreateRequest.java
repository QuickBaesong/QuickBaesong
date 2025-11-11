package com.qb.deliveryservice.application.dto;

import com.qb.deliveryservice.domain.model.Delivery;
import com.qb.deliveryservice.domain.model.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryCreateRequest {
    private UUID orderId;           // 주문 ID
    private String companyManagerId; // 업체 담당자
    private UUID departureHubId;     // 출발 허브
    private UUID destinationHubId; // 도착 허브
    private String deliveryAddress;  // 배송 주소
    private String recipientName;    // 수령인
    private String recipientSlackId; // 수령인 슬랙ID

    public Delivery toEntity() {
        return Delivery.builder()
                .id(UUID.randomUUID())
                .orderId(orderId)
                .departureHubId(departureHubId)
                .destinationHubId(destinationHubId)
                .deliveryAddress(deliveryAddress)
                .recipientName(recipientName)
                .recipientSlackId(recipientSlackId)
                .companyManagerId(companyManagerId)
                .currentStatus(DeliveryStatus.HUB_WAITING) // 최초 상태
                .build();
    }
}
