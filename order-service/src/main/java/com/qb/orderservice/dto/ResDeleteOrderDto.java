package com.qb.orderservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.qb.orderservice.domain.entity.Order;
import com.qb.orderservice.domain.entity.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResDeleteOrderDto {
	private UUID orderId;
	private LocalDateTime deletedAt;
	private String deletedBy;

	public static ResDeleteOrderDto fromEntity(Order order){
		return ResDeleteOrderDto.builder()
			.orderId(order.getOrderId())
			.deletedAt(order.getDeletedAt())
			.deletedBy(order.getDeletedBy())
			.build();
	}
}
