package com.qb.deliveryservice.application.dto;

import com.qb.deliveryservice.domain.model.DeliveryStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class DeliveryListResponse {
    private UUID deliveryId;
    private UUID orderId;
    private DeliveryStatus currentStatus;
    private String companyName;
    private String itemName;
    private String destinationHubName;
    private String deliveryManagerName;
    private String recipientName;
    private LocalDateTime createdAt;
}
