package com.qb.itemservice.dto;

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
public class ReqUpdateItemInfoDto {

	@NotNull(message = "가격은 비워둘 수 없습니다.")
	@Positive(message = "가격은 1원 미만일 수 없습니다.")
	private Long price;

	@NotBlank(message = "상품명은 비워둘 수 없습니다.")
	private String itemName;

	public Item toItem(UUID itemId){
		return new Item(
			itemId,
			this.price,
			this.itemName
		);
	}
}
