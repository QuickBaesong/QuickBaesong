package com.qb.deliveryservice.infrastructure.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class OrderResponse {
    private UUID orderId;
    private UUID supplierCompanyId;      // 업체명 조회용
    private LocalDateTime requiredDeliveryAt;  // 배송 요청 일시
    private String createdBy;            // 사용자명 조회용
    private List<OrderItem> orderItems;  // 상품 정보 및 수량

    @Getter
    @NoArgsConstructor
    public static class OrderItem {
        private UUID itemId;
        private Integer quantity;
    }
}
