package com.qb.orderservice.client;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.qb.common.response.ApiResponse;
import com.qb.orderservice.client.dto.ReqPatchItemDto;
import com.qb.orderservice.client.dto.ResGetItemDto;
import com.qb.orderservice.client.dto.ResPatchItemDto;

@FeignClient(name = "item-service")
public interface ItemServiceClient {

	@GetMapping("/v1/items/{itemId}")
	ApiResponse<ResGetItemDto> getItem(@PathVariable("itemId") UUID itemId);

	@PutMapping("/v1/items/decrease")
	ApiResponse<List<ResPatchItemDto>> decreaseQuantity(@RequestBody List<ReqPatchItemDto> itemList);

	@PutMapping("/v1/items/increase")
	ApiResponse<List<ResPatchItemDto>> increaseQuantity(@RequestBody List<ReqPatchItemDto> itemList);

}
