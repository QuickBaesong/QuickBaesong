package com.qb.deliveryservice.application.dto;

import com.qb.deliveryservice.domain.model.Delivery;
import com.qb.deliveryservice.domain.model.DeliveryManager;
import com.qb.deliveryservice.domain.model.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class DeliveryResponse {
    private UUID deliveryId;
    private UUID orderId;
    private DeliveryStatus currentStatus;
    private UUID departureHubId;
    private UUID destinationHubId;
    private String deliveryAddress;
    private String recipientName;
    private String recipientSlackId;
    private ManagerInfo hubManager;          // 허브 배송 담당자
    private ManagerInfo destinationManager;  // 목적지 업체 담당자
    private LocalDateTime createdAt;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ManagerInfo {
        private UUID managerId;
        private Integer sequence;
    }

    public static DeliveryResponse from(
            Delivery delivery,
            DeliveryManager hubManager,
            DeliveryManager destinationManager
    ) {
        return DeliveryResponse.builder()
                .deliveryId(delivery.getId())
                .orderId(delivery.getOrderId())
                .currentStatus(delivery.getCurrentStatus())
                .departureHubId(delivery.getDepartureHubId())
                .destinationHubId(delivery.getDestinationHubId())
                .deliveryAddress(delivery.getDeliveryAddress())
                .recipientName(delivery.getRecipientName())
                .recipientSlackId(delivery.getRecipientSlackId())
                .hubManager(ManagerInfo.builder()
                        .managerId(hubManager.getId())
                        .sequence(hubManager.getSequence())
                        .build())
                .destinationManager(ManagerInfo.builder()
                        .managerId(destinationManager.getId())
                        .sequence(destinationManager.getSequence())
                        .build())
                .createdAt(delivery.getCreatedAt())
                .build();
    }
}
