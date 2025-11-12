package com.qb.orderservice.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
	private List<ResGetOrderDto.RegGetOrderItemDto> orderItems;
	private UUID sender;
	private UUID receiver;
	private UUID hubId;
	private UUID deliveryId;
	private LocalDateTime requiredDeliveryAt;

	public static ResGetOrderDto fromEntity(Order order){

		List<RegGetOrderItemDto> orderItemDtos = order.getOrderItems().stream()
			.map(RegGetOrderItemDto::fromEntity)
			.collect(Collectors.toList());

		return ResGetOrderDto.builder()
			.orderId(order.getOrderId())
			.orderItems(orderItemDtos)
			.sender(order.getSender())
			.receiver(order.getReceiver())
			.hubId(order.getHubId())
			.deliveryId(order.getDeliveryId())
			.requiredDeliveryAt(order.getRequiredDeliveryAt())
			.build();
	}


	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class RegGetOrderItemDto {
		private UUID orderItemId;
		private UUID itemId;
		private Long quantity;
		private Long price;

		public static RegGetOrderItemDto fromEntity(OrderItem item) {
			return new RegGetOrderItemDto(
				item.getOrderItemId(),
				item.getItemId(),
				item.getQuantity(),
				item.getPrice()
			);
		}
	}

}
