package com.qb.orderservice.client.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.qb.orderservice.domain.entity.OrderItem;
import com.qb.orderservice.dto.ReqCreateOrderDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReqCreateDeliveryDto {
	private UUID orderId;
	private UUID sender;
	private UUID receiver;
	private UUID departureHubId;
	private String deliveryAddress;
	private String recipientName;
	private String recipientSlackId;
	private LocalDateTime requiredDeliveryAt;
	private List<ReqCreateDeliveryDto.OrderItemDto> orderItems;

	public static ReqCreateDeliveryDto fromOrderCreation(
		UUID orderId,
		ReqCreateOrderDto requestDto,
		List<OrderItem> orderItemList
	) {

		List<ReqCreateDeliveryDto.OrderItemDto> orderItems = orderItemList.stream()
			.map(ReqCreateDeliveryDto.OrderItemDto::fromOrderItem)
			.collect(Collectors.toList());

		return new ReqCreateDeliveryDto(
			orderId,
			requestDto.getSender(),
			requestDto.getReceiver(),
			requestDto.getHubId(),
			requestDto.getDeliveryAddress(),
			requestDto.getRecipientName(),
			requestDto.getRecipientSlackId(),
			requestDto.getRequiredDeliveryAt(),
			orderItems
		);
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OrderItemDto {
		private UUID orderItemId;
		private UUID itemId;
		private Long quantity;
		private Long price;

		public static OrderItemDto fromOrderItem(OrderItem orderItem) {
			return new OrderItemDto(
				orderItem.getOrderItemId(),
				orderItem.getItemId(),
				orderItem.getQuantity(),
				orderItem.getPrice()
			);
		}

	}
}
