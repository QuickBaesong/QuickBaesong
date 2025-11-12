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
	private UUID deliveryId;
	private UUID orderId;
	private String currentStatus;
	private UUID departureHubId;
	private UUID destinationHubId;
	private String deliveryAddress;
	private String recipientName;
	private String recipientSlackId;
	private String companyManagerId;
	private LocalDateTime createdAt;
}
