package com.qb.itemservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.qb.itemservice.domain.entity.Item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResCreateItemDto {
	private UUID itemId;
	private String itemName;
	private String companyName;
	private String hubName;
	private Long price;
	private Long quantity;
	private LocalDateTime createdAt;
	private String createdBy;

	public static ResCreateItemDto fromEntity(Item item, String hubName, String companyName) {
		if(item == null) return null;
		return ResCreateItemDto.builder()
			.itemId(item.getItemId())
			.itemName(item.getItemName())
			.companyName(companyName)
			.hubName(hubName)
			.price(item.getPrice())
			.quantity(item.getQuantity())
			.createdAt(item.getCreatedAt())
			.createdBy(item.getCreatedBy())
			.build();
	}
}
