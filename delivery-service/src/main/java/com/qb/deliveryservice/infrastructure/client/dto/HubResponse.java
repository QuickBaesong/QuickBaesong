package com.qb.deliveryservice.infrastructure.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class HubResponse {
    private UUID hubId;
    private String name;  // 허브명만 필요
}
