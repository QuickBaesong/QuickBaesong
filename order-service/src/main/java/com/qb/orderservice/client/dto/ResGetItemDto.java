package com.qb.orderservice.client.dto;

import java.time.LocalDateTime;
import java.util.UUID;

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
	
}
