package com.qb.orderservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReqPatchOrderItemDto {
	@Positive(message = "가격은 1원 미만일 수 없습니다.")
	@NotNull(message = "수정할 가격을 입력해주세요.")
	private Long price;
}
