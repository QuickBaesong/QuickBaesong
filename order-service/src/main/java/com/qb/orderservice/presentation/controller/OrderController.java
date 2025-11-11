package com.qb.orderservice.presentation.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qb.common.enums.SuccessCode;
import com.qb.common.response.ApiResponse;
import com.qb.orderservice.application.service.OrderService;
import com.qb.orderservice.dto.ReqCreateOrderDto;
import com.qb.orderservice.dto.ReqPatchOrderItemDto;
import com.qb.orderservice.dto.ResCreateOrderDto;
import com.qb.orderservice.dto.ResDeleteOrderDto;
import com.qb.orderservice.dto.ResGetOrderDto;
import com.qb.orderservice.dto.ResPatchOrderItemDto;

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

	@GetMapping("/{orderId}")
	public ApiResponse<ResGetOrderDto> getOrder(@PathVariable("orderId") UUID orderId){
		ResGetOrderDto resGetOrderDto = orderService.getOrder(orderId);
		return ApiResponse.of(SuccessCode.OK, resGetOrderDto);
	}

	@DeleteMapping("/{orderId}")
	public ApiResponse<ResDeleteOrderDto> deleteOrder(@PathVariable("orderId") UUID orderId){
		ResDeleteOrderDto resDeleteOrderDto = orderService.deleteOrder(orderId);
		return ApiResponse.of(SuccessCode.OK, resDeleteOrderDto);
	}

	@PatchMapping("/{orderId}/items/{orderItemId}")
	public ApiResponse<ResPatchOrderItemDto> patchOrderItem(@PathVariable("orderId") UUID orderId,
		@PathVariable("orderItemId") UUID orderItemId
		, @RequestBody @Valid ReqPatchOrderItemDto reqPatchOrderItemDto){
		ResPatchOrderItemDto resPatchOrderItemDto = orderService.patchOrderItem(orderId, orderItemId, reqPatchOrderItemDto);
		return ApiResponse.of(SuccessCode.OK, resPatchOrderItemDto);
	}
}
