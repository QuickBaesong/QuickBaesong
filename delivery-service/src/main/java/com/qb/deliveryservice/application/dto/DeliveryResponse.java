package com.qb.deliveryservice.application.dto;

import com.qb.deliveryservice.domain.model.Delivery;
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
    // private UUID companyManagerId;
    private LocalDateTime createdAt;

    public static DeliveryResponse from(Delivery delivery) {
        return DeliveryResponse.builder()
                .deliveryId(delivery.getId())
                .orderId(delivery.getOrderId())
                .currentStatus(delivery.getCurrentStatus())
                .departureHubId(delivery.getDepartureHubId())
                .destinationHubId(delivery.getDestinationHubId())
                .deliveryAddress(delivery.getDeliveryAddress())
                .recipientName(delivery.getRecipientName())
                .recipientSlackId(delivery.getRecipientSlackId())
//                .companyManagerId(delivery.getCompanyManagerId())
                .createdAt(delivery.getCreatedAt())
                .build();
    }
}
