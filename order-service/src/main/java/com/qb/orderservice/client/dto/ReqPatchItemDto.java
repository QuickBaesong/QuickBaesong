package com.qb.orderservice.client.dto;

import java.util.UUID;

import com.qb.orderservice.domain.entity.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReqPatchItemDto {
	private UUID itemId;
	private Long quantity;

	public static ReqPatchItemDto fromEntity(OrderItem orderItem) {
		return new ReqPatchItemDto(
			orderItem.getItemId(),
			orderItem.getQuantity()
		);
	}
}
