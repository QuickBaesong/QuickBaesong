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
public class ResDeleteItemDto {
	private UUID itemId;
	private String itemName;
	private String deletedBy;
	private LocalDateTime deletedAt;

	public static ResDeleteItemDto fromEntity(Item item){
		return ResDeleteItemDto.builder()
			.itemId(item.getItemId())
			.itemName(item.getItemName())
			.deletedBy(item.getDeletedBy())
			.deletedAt(LocalDateTime.now())
			.build();
	}
}
