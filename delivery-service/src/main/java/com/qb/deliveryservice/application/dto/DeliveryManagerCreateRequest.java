package com.qb.deliveryservice.application.dto;

import com.qb.deliveryservice.domain.model.DeliveryManager;
import com.qb.deliveryservice.domain.model.ManagerType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryManagerCreateRequest {
    private UUID userId;
    private UUID hubId;
    private ManagerType type;

    public DeliveryManager toEntity(String slackId, Integer sequence) {
        return DeliveryManager.builder()
                .userId(userId)
                .hubId(hubId)
                .managerType(type)
                .slackId(slackId)
                .sequence(sequence)
                .build();
    }
}
