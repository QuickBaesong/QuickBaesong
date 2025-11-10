package com.qb.itemservice.presentation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qb.common.enums.SuccessCode;
import com.qb.common.response.ApiResponse;
import com.qb.itemservice.application.service.ItemService;
import com.qb.itemservice.dto.ReqCreateItemDto;
import com.qb.itemservice.dto.ReqPatchItemDto;
import com.qb.itemservice.dto.ResCreateItemDto;
import com.qb.itemservice.dto.ResGetItemDto;
import com.qb.itemservice.dto.ResPatchItemDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/items")
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;

	@PostMapping
	public ApiResponse<ResCreateItemDto> createItem(@RequestBody @Valid ReqCreateItemDto requestDto){
		return ApiResponse.of(SuccessCode.CREATED, itemService.createItem(requestDto));
	}

	@GetMapping("/{itemId}")
	public ApiResponse<ResGetItemDto> getItem(@PathVariable UUID itemId){
		return ApiResponse.of(SuccessCode.OK, itemService.getItem(itemId));
	}

	@PatchMapping("/decrease")
	public ApiResponse<List<ResPatchItemDto>> decreaseQuantity(@RequestBody List<ReqPatchItemDto> itemList){
		return ApiResponse.of(SuccessCode.OK, itemService.decreaseQuantity(itemList));
	}

	@PatchMapping("/increase")
	public ApiResponse<List<ResPatchItemDto>> increaseQuantity(@RequestBody List<ReqPatchItemDto> itemList){
		return ApiResponse.of(SuccessCode.OK, itemService.decreaseQuantity(itemList));
	}


}
