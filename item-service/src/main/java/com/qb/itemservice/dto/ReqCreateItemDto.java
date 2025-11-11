package com.qb.itemservice.dto;

import java.util.UUID;

import com.qb.itemservice.domain.entity.Item;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqCreateItemDto {

	@NotBlank(message = "등록할 상품 이름을 비워둘 수 없습니다.")
	@Size(min = 2, max = 20, message = "상품 이름은 2글자 이상 20자 이하로만 등록 가능합니다.")
	private String itemName;

	@NotNull(message = "관리 허브를 선택해주세요")
	private UUID hubId;

	@NotNull(message = "업체 정보를 입력해주세요")
	private UUID companyId;

	@NotNull(message = "상품 가격을 입력해주세요.")
	@Positive(message = "가격은 1원 미만일 수 없습니다.")
	private Long price;

	@NotNull(message = "수량을 비워둘 수 없습니다.")
	@Positive(message = "수량은 1개 미만일 수 없습니다.")
	private Long quantity;

	public Item toEntity(){
		return Item.builder()
			.itemName(this.itemName)
			.hubId(this.hubId)
			.companyId(this.companyId)
			.price(this.price)
			.quantity(this.quantity)
			.build();
	}

}
