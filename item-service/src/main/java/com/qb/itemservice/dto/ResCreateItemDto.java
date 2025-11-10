package com.qb.itemservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResCreateItemDto {
	private UUID itemId;
	private String itemName;
	private String hubName;
	private String companyName;
	private Long price;
	private Long quantity;
	private LocalDateTime createdAt;
	private String createdBy;

	public ResCreateItemDto(UUID itemId, String itemName, String hubName, String companyName, Long price, Long quantity) {
		this.itemId = itemId;
		this.itemName = itemName;
		this.hubName = hubName;
		this.companyName = companyName;
		this.price = price;
		this.quantity = quantity;
	}
}
