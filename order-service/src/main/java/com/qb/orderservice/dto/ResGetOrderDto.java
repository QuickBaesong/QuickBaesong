package com.qb.orderservice.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
public class ResGetOrderDto {
	private UUID orderId;
	private List<OrderItem> orderItems = new ArrayList<>();
	private UUID sender;
	private UUID receiver;
	private UUID hubId;
	private UUID deliveryId;
	private LocalDateTime requiredDeliveryAt;

	public static ResGetOrderDto fromEntity(Order order){
		return ResGetOrderDto.builder()
			.orderId(order.getOrderId())
			.orderItems(order.getOrderItems())
			.sender(order.getSender())
			.receiver(order.getReceiver())
			.hubId(order.getHubId())
			.deliveryId(order.getDeliveryId())
			.requiredDeliveryAt(order.getRequiredDeliveryAt())
			.build();
	}
}
