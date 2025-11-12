package com.qb.orderservice.client.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginContext;

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
	private UUID departureHubId;
	private UUID destinationHubId;
	private String deliveryAddress;
	private String recipientName;
	private String recipientSlackId;


	public static ReqCreateDeliveryDto fromOrderCreation(
		UUID orderId,
		ReqCreateOrderDto requestDto
	) {

		return new ReqCreateDeliveryDto(
			orderId,
			requestDto.getHubId(),
			requestDto.getDestinationHubId(),
			requestDto.getDeliveryAddress(),
			requestDto.getRecipientName(),
			requestDto.getRecipientSlackId()
		);
	}

}
