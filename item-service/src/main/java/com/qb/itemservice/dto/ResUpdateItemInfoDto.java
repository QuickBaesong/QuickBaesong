package com.qb.itemservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.qb.itemservice.domain.entity.Item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResUpdateItemInfoDto {

	private UUID itemId;
	private String itemName;
	private Long price;
	private LocalDateTime updatedAt;
	private String updatedBy;

	public static ResUpdateItemInfoDto toDto(Item item) {
		return new ResUpdateItemInfoDto(
			item.getItemId(),
			item.getItemName(),
			item.getPrice(),
			item.getUpdatedAt(),
			item.getUpdatedBy()
		);
	}

}
