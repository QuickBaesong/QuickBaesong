package com.qb.itemservice.dto;

import java.util.UUID;

import com.qb.itemservice.domain.entity.Item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResSearchItem {

	private UUID itemId;
	private String itemName;
	private Long price;
	private Long quantity;
	private UUID companyId;
	private UUID hubId;

	public static ResSearchItem fromEntity(Item item) {
		return new ResSearchItem(
			item.getItemId(),
			item.getItemName(),
			item.getPrice(),
			item.getQuantity(),
			item.getCompanyId(),
			item.getHubId()
		);
	}
}
