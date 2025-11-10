package com.qb.orderservice.presentation.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qb.common.enums.SuccessCode;
import com.qb.common.response.ApiResponse;
import com.qb.orderservice.application.service.OrderService;
import com.qb.orderservice.dto.ReqCreateOrderDto;
import com.qb.orderservice.dto.ResCreateOrderDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public ApiResponse<ResCreateOrderDto> createOrder(@RequestBody @Valid ReqCreateOrderDto requestDto){
		ResCreateOrderDto newOrder = orderService.createOrder(requestDto);
		return ApiResponse.of(SuccessCode.CREATED, newOrder);
	}
}
