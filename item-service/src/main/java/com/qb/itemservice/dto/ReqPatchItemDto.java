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
@Builder
public class ReqPatchItemDto {
	private UUID itemId;
	private Long quantity;

	public Item toEntity(){
		return new Item(itemId, quantity);
	}
}
