package com.qb.orderservice.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qb.common.enums.ErrorCode;
import com.qb.common.enums.SuccessCode;
import com.qb.common.response.ApiResponse;
import com.qb.orderservice.client.DeliveryServiceClient;
import com.qb.orderservice.client.ItemServiceClient;
import com.qb.orderservice.client.dto.ReqCreateDeliveryDto;
import com.qb.orderservice.client.dto.ReqPatchItemDto;
import com.qb.orderservice.client.dto.ResCreateDeliveryDto;
import com.qb.orderservice.client.dto.ResGetItemDto;
import com.qb.orderservice.domain.entity.Order;
import com.qb.orderservice.domain.entity.OrderItem;
import com.qb.orderservice.domain.repository.OrderRepository;
import com.qb.orderservice.dto.ReqCreateOrderDto;
import com.qb.orderservice.dto.ReqPatchOrderItemDto;
import com.qb.orderservice.dto.ResCreateOrderDto;
import com.qb.orderservice.dto.ResDeleteOrderDto;
import com.qb.orderservice.dto.ResGetOrderDto;
import com.qb.orderservice.dto.ResPatchOrderItemDto;
import com.qb.orderservice.exception.OrderCustomException;
import com.qb.orderservice.exception.OrderErrorCode;

import feign.FeignException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;

	private final ItemServiceClient itemServiceClient;

	private final DeliveryServiceClient deliveryServiceClient;

	private static final int MAX_RETRIES = 3;
	private static final long RETRY_DELAY_MS = 100;

	@Transactional
	public ResCreateOrderDto createOrder(ReqCreateOrderDto requestDto) {

		// TODO : sender 직원 / receiver 직원 검증

		List<OrderItem> orderItems = new ArrayList<>();
		Order order = null;

		try {

			order = requestDto.toEntity();

			for (ReqCreateOrderDto.OrderItemDto orderItemDto : requestDto.getOrderItems()) {

				ApiResponse<ResGetItemDto> getItemDtoResponse = itemServiceClient.getItem(orderItemDto.getItemId());

				if (getItemDtoResponse.getData() == null) {
					throw new OrderCustomException(OrderErrorCode.NOT_FOUND_ITEM);
				}

				ResGetItemDto getItemDto = getItemDtoResponse.getData();

				if (getItemDto.getQuantity() < orderItemDto.getQuantity()) {
					throw new OrderCustomException(OrderErrorCode.OUT_OF_STOCK);
				}

				OrderItem orderItem = orderItemDto.toEntity();
				order.addOrderItem(orderItem);
				orderItems.add(orderItem);

			}

			orderRepository.save(order);

			List<ReqPatchItemDto> decreaseList = requestDto.getOrderItems().stream()
				.map(ReqCreateOrderDto.OrderItemDto::toReqUpdateItemStockDto)
				.collect(Collectors.toList());

			this.decreaseStockWithRetry(decreaseList);

			// TO DO : 로그인 userName 가져오기
			ResCreateDeliveryDto delivery = this.createDeliveryWithRetry(order.getOrderId(), requestDto, "tester");
			order.updateDeliveryInfo(delivery.getDeliveryId());

			ResCreateOrderDto resCreateOrderDto = ResCreateOrderDto.fromEntity(order, delivery);

			return resCreateOrderDto;

		} catch (Exception e) {
			log.error("주문 생성 중 오류 발생: {}", e.getMessage(), e);

			if (orderItems.size() > 0) {
				this.compensateStock(orderItems, order.getOrderId());
			}

			if(order != null) {
				// 주문 삭제
				order.softDelete("SYSTEM_ERROR");
			}

			throw new OrderCustomException(OrderErrorCode.ORDER_CREATION_FAILED);
		}

	}

	@Transactional(readOnly = true)
	public ResGetOrderDto getOrder(UUID orderId) {
		return ResGetOrderDto.fromEntity(
			orderRepository.findByOrderIdAndDeletedAtIsNull(orderId)
				.orElseThrow(() -> new OrderCustomException(OrderErrorCode.NOT_FOUND_ORDER))
		);
	}

	@Transactional
	public ResDeleteOrderDto deleteOrder(UUID orderId) {
		Order order = orderRepository.findByOrderIdAndDeletedAtIsNull(orderId)
			.orElseThrow(() -> new OrderCustomException(OrderErrorCode.NOT_FOUND_ORDER));

		// 업체담당자일때 주문자 != 로그인유저 -> 실패
			// 취소
		//order.canceledOrder("");

		order.softDelete("");

		return ResDeleteOrderDto.fromEntity(order);

	}

	@Transactional
	public ResPatchOrderItemDto patchOrderItem(UUID orderId, UUID orderItemId, @Valid ReqPatchOrderItemDto reqPatchOrderItemDto) {
		Order order = orderRepository.findByOrderIdAndDeletedAtIsNull(orderId)
			.orElseThrow(()->new OrderCustomException(OrderErrorCode.NOT_FOUND_ORDER));

		Map<UUID, OrderItem> orderItemMap = order.getOrderItems()
											.stream()
											.collect(Collectors.toMap(OrderItem::getOrderItemId, Function.identity()));

		OrderItem orderItem = orderItemMap.get(orderItemId);

		if (orderItem == null || orderItem.getDeletedAt() != null) {
			throw new OrderCustomException(OrderErrorCode.NOT_FOUND_ORDER_ITEM);
		}

		orderItem.updateOrderItemPrice(reqPatchOrderItemDto.getPrice());

		return ResPatchOrderItemDto.fromEntity(order, orderItem);
	}

	private void decreaseStockWithRetry(List<ReqPatchItemDto> requestList) {
		int attempt = 0;
		while (attempt < MAX_RETRIES) {
			try {
				itemServiceClient.decreaseQuantity(requestList);
				return; // 성공 시 종료
			} catch (FeignException e) {
				if (e.status() == 400) {
					// 재고 부족 등의 400 에러는 재시도하지 않고 즉시 실패 (비즈니스 오류)
					log.error("재고 감소 400 오류: 요청 목록에 문제 발생", e);
					throw new OrderCustomException(OrderErrorCode.INVALID_ORDER_REQUEST);
				}
				attempt++;
				log.warn("재고 감소 실패 ({}회 시도), 재시도...", attempt);
				this.sleep(RETRY_DELAY_MS);
			}
		}
		log.error("ItemService.decreaseQuantity 호출 최종 실패.");
		throw new OrderCustomException(OrderErrorCode.ITEM_SERVICE_UNAVAILABLE);
	}

	private ResCreateDeliveryDto createDeliveryWithRetry(UUID orderId, ReqCreateOrderDto requestDto, String companyManagerId) {
		ReqCreateDeliveryDto reqCreateDeliveryDto = ReqCreateDeliveryDto.fromOrderCreation(
			orderId,
			requestDto,
			companyManagerId
		);

		int attempt = 0;
		while (attempt < MAX_RETRIES) {
			try {
				ApiResponse<ResCreateDeliveryDto> response = deliveryServiceClient.createDelivery(reqCreateDeliveryDto);
				if (response.getData() == null) {
					throw new OrderCustomException(OrderErrorCode.INVALID_DELIVERY_REQUEST);
				}
				return response.getData();
			} catch (FeignException e) {
				if (e.status() >= 400 && e.status() < 500) {
					log.error("배송 요청 4xx 오류: Order ID: {}", orderId, e);
					throw new OrderCustomException(OrderErrorCode.INVALID_DELIVERY_REQUEST);
				}
				attempt++;
				log.warn("배송 요청 실패 ({}회 시도), 재시도...", attempt);
				this.sleep(RETRY_DELAY_MS);
			}
		}
		log.error("DeliveryService.createDelivery 호출 최종 실패. Order ID: {}", orderId);
		throw new OrderCustomException(OrderErrorCode.DELIVERY_SERVICE_UNAVAILABLE);
	}

	private void compensateStock(List<OrderItem> orderItems, UUID orderId) {

		List<ReqPatchItemDto> increaseList = orderItems.stream()
			.map(item -> ReqPatchItemDto.fromEntity(item))
			.collect(Collectors.toList());

		try {
			// 재고 복원 요청 (증가)
			itemServiceClient.increaseQuantity(increaseList);
		} catch (Exception compensationFailed) {
			log.error("치명적 오류: 재고 복원(COMPENSATION) 실패. Order ID: {}", orderId, compensationFailed);
		}

	}

	private void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}
}
