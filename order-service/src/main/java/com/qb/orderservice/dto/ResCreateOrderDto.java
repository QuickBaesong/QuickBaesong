package com.qb.orderservice.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
	private List<OrderItem> orderItems;
	private String totalAmount;
	private String totalPrice;
	private String deliveryAddress;
	private String recipientSlackId;
	private String recipientName;
	private LocalDateTime requiredDeliveryAt;

	public static ResCreateOrderDto fromEntity(Order order, ResCreateDeliveryDto deliveryDto) {
		return new ResCreateOrderDto(
			order.getOrderId(),
			order.getHubId(),
			order.getSender(),
			order.getReceiver(),
			order.getOrderItems(),
			order.calculateTotalAmount(),
			order.calculateTotalPrice(),
			deliveryDto.getDeliveryAddress(),
			deliveryDto.getRecipientSlackId(),
			deliveryDto.getRecipientName(),
			deliveryDto.getRequiredDeliveryAt()
		);
	}
}
