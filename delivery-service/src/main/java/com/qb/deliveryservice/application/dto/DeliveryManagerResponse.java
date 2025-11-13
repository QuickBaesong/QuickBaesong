package com.qb.deliveryservice.application.dto;

import com.qb.deliveryservice.domain.model.DeliveryManager;
import com.qb.deliveryservice.domain.model.ManagerType;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class DeliveryManagerResponse {
    private UUID deliveryManagerId;
    private UUID hubId;
    private ManagerType type;
    private String slackId;
    private Integer sequence; // 배송 순번 - 0부터 시작하여 순차적으로 증가

    public static DeliveryManagerResponse from(DeliveryManager manager) {
        return DeliveryManagerResponse.builder()
                .deliveryManagerId(manager.getId())
                .hubId(manager.getHubId())
                .type(manager.getManagerType())
                .slackId(manager.getSlackId())
                .sequence(manager.getSequence())
                .build();
    }
}
