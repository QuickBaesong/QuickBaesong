package com.qb.orderservice.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.qb.orderservice.client.dto.ResCreateDeliveryDto;
import com.qb.orderservice.domain.entity.Order;
import com.qb.orderservice.domain.entity.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResCreateOrderDto {

	private UUID orderId;
	private UUID hubId;
	private UUID sender;
	private UUID receiver;
	private List<ResCreateOrderDto.ResOrderItemDto> orderItems;
	private String totalAmount;
	private String totalPrice;
	private String deliveryAddress;
	private String recipientSlackId;
	private String recipientName;
	private LocalDateTime requiredDeliveryAt;

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ResOrderItemDto {
		private UUID orderItemId;
		private UUID itemId;
		private Long quantity;
		private Long price;

		public static ResOrderItemDto fromEntity(OrderItem orderItem) {
			return new ResOrderItemDto(
				orderItem.getOrderItemId(),
				orderItem.getItemId(),
				orderItem.getQuantity(),
				orderItem.getPrice()
			);
		}
	}

	public static ResCreateOrderDto fromEntity(Order order, ResCreateDeliveryDto deliveryDto) {

		List<ResOrderItemDto> resOrderItemDtoList = order.getOrderItems()
			.stream()
			.map(ResOrderItemDto::fromEntity)
			.collect(Collectors.toList());

		return new ResCreateOrderDto(
			order.getOrderId(),
			order.getHubId(),
			order.getSender(),
			order.getReceiver(),
			resOrderItemDtoList,
			order.calculateTotalAmount(),
			order.calculateTotalPrice(),
			deliveryDto.getDeliveryAddress(),
			deliveryDto.getRecipientSlackId(),
			deliveryDto.getRecipientName(),
			order.getRequiredDeliveryAt()
		);
	}
}
