package com.qb.orderservice.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qb.common.enums.SuccessCode;
import com.qb.common.response.ApiResponse;
import com.qb.orderservice.client.DeliveryServiceClient;
import com.qb.orderservice.client.ItemServiceClient;
import com.qb.orderservice.client.dto.ReqCreateDeliveryDto;
import com.qb.orderservice.client.dto.ReqUpdateItemStockDto;
import com.qb.orderservice.client.dto.ResCreateDeliveryDto;
import com.qb.orderservice.client.dto.ResGetItemDto;
import com.qb.orderservice.domain.entity.Order;
import com.qb.orderservice.domain.entity.OrderItem;
import com.qb.orderservice.domain.repository.OrderRepository;
import com.qb.orderservice.dto.ReqCreateOrderDto;
import com.qb.orderservice.dto.ResCreateOrderDto;

import feign.FeignException;
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

		// 주문 생성은 업체 담당자만 가능하다. role 검증
		// if (userRole != UserRole.COMPANY_MANAGER) {
		// 	throw new AccessDeniedException("업체 담당자만 주문을 생성할 수 있습니다.");
		// }

		List<OrderItem> orderItems = new ArrayList<>();
		Order order = null;

		try {

			order = requestDto.toEntity();

			for (ReqCreateOrderDto.OrderItemDto orderItemDto : requestDto.getOrderItems()) {

				ApiResponse<ResGetItemDto> getItemDtoResponse = itemServiceClient.getItem(orderItemDto.getItemId());

				if (getItemDtoResponse.getData() == null || getItemDtoResponse.getCode() != SuccessCode.OK) {
					throw new IllegalArgumentException("상품 정보를 찾을 수 없습니다.");
				}

				ResGetItemDto getItemDto = getItemDtoResponse.getData();

				if (getItemDto.getQuantity() < orderItemDto.getQuantity()) {
					throw new IllegalArgumentException("재고가 부족합니다");
				}

				OrderItem orderItem = orderItemDto.toEntity();
				order.addOrderItem(orderItem);
				orderItems.add(orderItem);

			}

			orderRepository.save(order);

			List<ReqUpdateItemStockDto> decreaseList = requestDto.getOrderItems().stream()
				.map(ReqCreateOrderDto.OrderItemDto::toReqUpdateItemStockDto)
				.collect(Collectors.toList());

			this.decreaseStockWithRetry(decreaseList);

			ResCreateDeliveryDto delivery = this.createDeliveryWithRetry(order.getOrderId(), requestDto, orderItems);
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

			throw new RuntimeException("주문 생성에 실패했습니다.");
		}

	}

	private void decreaseStockWithRetry(List<ReqUpdateItemStockDto> requestList) {
		int attempt = 0;
		while (attempt < MAX_RETRIES) {
			try {
				itemServiceClient.decreaseQuantity(requestList);
				return; // 성공 시 종료
			} catch (FeignException e) {
				if (e.status() == 400) {
					// 재고 부족 등의 400 에러는 재시도하지 않고 즉시 실패 (비즈니스 오류)
					log.error("재고 감소 400 오류: 요청 목록에 문제 발생", e);
					throw new IllegalArgumentException("재고 감소 요청 중 비즈니스 오류 발생");
				}
				attempt++;
				log.warn("재고 감소 실패 ({}회 시도), 재시도...", attempt);
				this.sleep(RETRY_DELAY_MS);
			}
		}
		log.error("ItemService.decreaseQuantity 호출 최종 실패.");
		throw new RuntimeException("재고 감소 서비스에 연결할 수 없습니다.");
	}

	private ResCreateDeliveryDto createDeliveryWithRetry(UUID orderId, ReqCreateOrderDto requestDto, List<OrderItem> succeededItems) {
		ReqCreateDeliveryDto reqCreateDeliveryDto = ReqCreateDeliveryDto.fromOrderCreation(
			orderId,
			requestDto,
			succeededItems
		);

		int attempt = 0;
		while (attempt < MAX_RETRIES) {
			try {
				ApiResponse<ResCreateDeliveryDto> response = deliveryServiceClient.createDelivery(reqCreateDeliveryDto);
				if (response.getData() == null || response.getCode() != SuccessCode.OK) {
					throw new IllegalArgumentException("배송 요청 처리에 실패했습니다");
				}
				return response.getData();
			} catch (FeignException e) {
				if (e.status() >= 400 && e.status() < 500) {
					log.error("배송 요청 4xx 오류: Order ID: {}", orderId, e);
					throw new IllegalArgumentException("배송 요청 중 오류 발생 (유효하지 않은 요청)");
				}
				attempt++;
				log.warn("배송 요청 실패 ({}회 시도), 재시도...", attempt);
				this.sleep(RETRY_DELAY_MS);
			}
		}
		log.error("DeliveryService.createDelivery 호출 최종 실패. Order ID: {}", orderId);
		throw new RuntimeException("배송 서비스에 연결할 수 없습니다.");
	}

	private void compensateStock(List<OrderItem> orderItems, UUID orderId) {

		List<ReqUpdateItemStockDto> increaseList = orderItems.stream()
			.map(item -> ReqUpdateItemStockDto.fromEntity(item))
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
