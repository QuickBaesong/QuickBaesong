package com.qb.orderservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.qb.orderservice.domain.entity.Order;
import com.qb.orderservice.domain.entity.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResPatchOrderItemDto {
	private UUID orderId;
	private UUID orderItemId;
	private LocalDateTime updatedAt;
	private String updatedBy;

	public static ResPatchOrderItemDto fromEntity(Order order, OrderItem orderItem) {
		return ResPatchOrderItemDto.builder()
			.orderId(order.getOrderId())
			.orderItemId(orderItem.getOrderItemId())
			.updatedAt(orderItem.getUpdatedAt())
			.updatedBy(orderItem.getUpdatedBy())
			.build();
	}
}
