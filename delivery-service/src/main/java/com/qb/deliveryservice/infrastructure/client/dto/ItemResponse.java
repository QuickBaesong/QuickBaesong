package com.qb.deliveryservice.infrastructure.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ItemResponse {
    private UUID itemId;
    private String itemName;  // 상품명만 필요
}
