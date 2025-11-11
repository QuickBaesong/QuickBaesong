package com.qb.deliveryservice.application.dto;

import com.qb.deliveryservice.domain.model.Delivery;
import com.qb.deliveryservice.domain.model.DeliveryStatus;
import com.qb.deliveryservice.infrastructure.client.dto.OrderResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class DeliveryDetailResponse {
    private UUID deliveryId;
    private OrderInfo order;
    private UUID deliveryManagerId;
    private String deliveryManagerName;
    private UUID companyId;
    private String companyName;
    private String userId;
    private String userName;
    private UUID itemId;
    private String itemName;
    private UUID hubId;
    private String hubName;
    private DeliveryStatus currentStatus;
    private String deliveryAddress;
    private String recipientName;
    private String recipientSlackId;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    @Getter
    @Builder
    public static class OrderInfo {
        private UUID orderId;
        private Integer orderQuantity;
        private LocalDateTime requiredDeliveryAt;

        public static OrderInfo from(OrderResponse order) {
            return OrderInfo.builder()
                    .orderId(order.getOrderId())
                    .orderQuantity(calculateTotalQuantity(order))
                    .requiredDeliveryAt(order.getRequiredDeliveryAt())
                    .build();
        }

        private static Integer calculateTotalQuantity(OrderResponse order) {
            if (order.getOrderItems() == null) return 0;
            return order.getOrderItems().stream()
                    .mapToInt(OrderResponse.OrderItem::getQuantity)
                    .sum();
        }
    }
}