package com.qb.itemservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.qb.itemservice.domain.entity.Item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResGetItemDto {
	private UUID itemId;
	private String itemName;
	private Long price;
	private Long quantity;
	private UUID hubId;
	private UUID companyId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static ResGetItemDto fromEntity(Item item){

		return ResGetItemDto.builder()
			.itemId(item.getItemId())
			.itemName(item.getItemName())
			.price(item.getPrice())
			.quantity(item.getQuantity())
			.hubId(item.getHubId())
			.companyId(item.getCompanyId())
			.createdAt(item.getCreatedAt())
			.updatedAt(item.getUpdatedAt())
			.build();
	}
}
