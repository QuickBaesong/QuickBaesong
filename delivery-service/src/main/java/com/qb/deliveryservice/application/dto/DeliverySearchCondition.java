package com.qb.deliveryservice.application.dto;

import com.qb.deliveryservice.domain.model.DeliveryStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class DeliverySearchCondition {
    private DeliveryStatus status;
    private UUID managerId;
    private UUID hubId;
}
