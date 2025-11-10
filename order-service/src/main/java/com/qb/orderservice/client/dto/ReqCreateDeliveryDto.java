package com.qb.orderservice.client.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
	private List<OrderItem> orderItems;

	public static ReqCreateDeliveryDto fromOrderCreation(
		UUID orderId,
		ReqCreateOrderDto requestDto,
		List<OrderItem> orderItemList
	) {
		return new ReqCreateDeliveryDto(
			orderId,
			requestDto.getSender(),
			requestDto.getReceiver(),
			requestDto.getHubId(),
			requestDto.getDeliveryAddress(),
			requestDto.getRecipientName(),
			requestDto.getRecipientSlackId(),
			requestDto.getRequiredDeliveryAt(),
			orderItemList
		);
	}
}
