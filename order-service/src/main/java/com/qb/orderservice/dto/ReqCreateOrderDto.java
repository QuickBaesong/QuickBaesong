package com.qb.orderservice.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.qb.orderservice.client.dto.ReqPatchItemDto;
import com.qb.orderservice.domain.entity.Order;
import com.qb.orderservice.domain.entity.OrderItem;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReqCreateOrderDto {

	@NotNull(message = "관리 허브를 비워둘 수 없습니다.")
	private UUID hubId;

	@NotNull(message = "도착 허브를 비워둘 수 없습니다.")
	private UUID destinationHubId;

	@NotNull(message = "공급 업체를 비워둘 수 없습니다.")
	private UUID sender;

	@NotNull(message = "수령 업체를 비워둘 수 없습니다.")
	private UUID receiver;

	@NotNull(message = "주문자를 비워둘 수 없습니다.")
	private UUID userId;

	@NotNull(message = "요청 납품기한을 비워둘 수 없습니다.")
	private LocalDateTime requiredDeliveryAt;

	@NotEmpty(message = "주문할 상품을 1개 이상 선택해야합니다.")
	@Valid
	List<OrderItemDto> orderItems = new ArrayList<>();

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OrderItemDto{

		@NotNull(message = "주문할 상품을 선택해주세요")
		private UUID itemId;

		@NotNull(message = "수량은 비워둘 수 없습니다.")
		@Positive(message = "수량은 1개 미만일 수 없습니다.")
		private Long quantity;

		@NotNull(message = "가격은 비워둘 수 없습니다.")
		@Positive(message = "가격은 1개 미만일 수 없습니다.")
		private Long price;

		public ReqPatchItemDto toReqUpdateItemStockDto() {
			return new ReqPatchItemDto(this.itemId, this.quantity);
		}

		public OrderItem toEntity() {
			return OrderItem.builder()
				.itemId(this.itemId)
				.quantity(this.quantity)
				.price(this.price)
				.build();
		}

	}

	@NotBlank(message = "배송지를 입력해주세요")
	@Size(min = 10, max = 50)
	private String deliveryAddress;

	@NotBlank(message = "Slack ID는 비워둘 수 없습니다.")
	private String recipientSlackId;

	@NotBlank(message = "수령자 이름은 비워둘 수 없습니다.")
	private String recipientName;

	public Order toEntity() {
		return Order.builder()
			.sender(this.sender)
			.receiver(this.receiver)
			.hubId(this.hubId)
			.userId(this.userId)
			.requiredDeliveryAt(this.requiredDeliveryAt)
			.build();
	}



}
