package com.qb.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.qb.common.response.ApiResponse;
import com.qb.orderservice.client.dto.ReqCreateDeliveryDto;
import com.qb.orderservice.client.dto.ResCreateDeliveryDto;

@FeignClient(name = "delivery-service")
public interface DeliveryServiceClient {

	@PostMapping("/internal/v1/deliveries")
	ApiResponse<ResCreateDeliveryDto> createDelivery(@RequestBody ReqCreateDeliveryDto reqCreateDeliveryDto);
}
