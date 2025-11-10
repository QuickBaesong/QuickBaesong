package com.qb.orderservice.client.dto;

import java.util.UUID;

import com.qb.orderservice.domain.entity.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReqUpdateItemStockDto {
	private UUID itemId;
	private Long quantity;

	public static ReqUpdateItemStockDto fromEntity(OrderItem orderItem) {
		return new ReqUpdateItemStockDto(
			orderItem.getItemId(),
			orderItem.getQuantity()
		);
	}
}
