package com.qb.orderservice.presentation.controller;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qb.common.enums.SuccessCode;
import com.qb.common.response.ApiResponse;
import com.qb.common.response.PageResponse;
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
	private static final List<Integer> ALLOWED_SIZES = Arrays.asList(10, 30, 50);
	private static final int DEFAULT_SIZE = 10;

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

	@GetMapping
	public ApiResponse<PageResponse<ResGetOrderDto>> searchOrders(@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size){
		int requestSize = size;
		int finalSize = ALLOWED_SIZES.contains(requestSize) ? requestSize : DEFAULT_SIZE;
		int zeroBasedPage = Math.max(0, page - 1);

		Pageable finalPageable = PageRequest.of(
			zeroBasedPage,
			finalSize
		);

		// UserContext userContext = getCurrentUserContext();

		Page<ResGetOrderDto> resultPage = orderService.searchOrders(finalPageable/*, userContext*/);
		PageResponse<ResGetOrderDto> responseDto = PageResponse.from(resultPage);

		return ApiResponse.of(SuccessCode.OK, responseDto);

	}
}
