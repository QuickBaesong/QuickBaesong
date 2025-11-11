package com.qb.orderservice.client.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResCreateDeliveryDto {
	private UUID orderId;
	private UUID deliveryId;
	private String deliveryAddress;
	private String recipientSlackId;
	private String recipientName;
	private LocalDateTime requiredDeliveryAt;
	private String currentStatus;
}
