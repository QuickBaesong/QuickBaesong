package com.qb.itemservice.presentation.controller;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qb.common.annotations.CurrentUser;
import com.qb.common.annotations.RequiredRole;
import com.qb.common.enums.SuccessCode;
import com.qb.common.enums.UserRole;
import com.qb.common.response.ApiResponse;
import com.qb.common.response.PageResponse;
import com.qb.common.security.UserContext;
import com.qb.itemservice.application.service.ItemService;
import com.qb.itemservice.dto.ReqCreateItemDto;
import com.qb.itemservice.dto.ReqPatchItemDto;
import com.qb.itemservice.dto.ReqUpdateItemInfoDto;
import com.qb.itemservice.dto.ResCreateItemDto;
import com.qb.itemservice.dto.ResDeleteItemDto;
import com.qb.itemservice.dto.ResGetItemDto;
import com.qb.itemservice.dto.ResPatchItemDto;
import com.qb.itemservice.dto.ResSearchItem;
import com.qb.itemservice.dto.ResUpdateItemInfoDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/items")
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;
	private static final List<Integer> ALLOWED_SIZES = Arrays.asList(10, 30, 50);
	private static final int DEFAULT_SIZE = 10;

	@PostMapping
	@RequiredRole({UserRole.MASTER, UserRole.HUB_MANAGER, UserRole.SUPPLIER_MANAGER})
	public ApiResponse<ResCreateItemDto> createItem(@RequestBody @Valid ReqCreateItemDto requestDto
		, @CurrentUser UserContext user){
		return ApiResponse.of(SuccessCode.CREATED, itemService.createItem(requestDto, user));
	}

	@GetMapping("/{itemId}")
	@RequiredRole({UserRole.MASTER, UserRole.HUB_MANAGER, UserRole.SUPPLIER_MANAGER, UserRole.DELIVERY_MANAGER})
	public ApiResponse<ResGetItemDto> getItem(@PathVariable UUID itemId
		, @CurrentUser UserContext user
	){
		return ApiResponse.of(SuccessCode.OK, itemService.getItem(itemId, user));
	}

	@PutMapping("/decrease")
	@RequiredRole({UserRole.MASTER, UserRole.HUB_MANAGER, UserRole.SUPPLIER_MANAGER})
	public ApiResponse<List<ResPatchItemDto>> decreaseQuantity(@RequestBody List<ReqPatchItemDto> itemList
		, @CurrentUser UserContext user
	){
		return ApiResponse.of(SuccessCode.OK, itemService.decreaseQuantity(itemList, user));
	}

	@PutMapping("/increase")
	@RequiredRole({UserRole.MASTER, UserRole.HUB_MANAGER, UserRole.SUPPLIER_MANAGER})
	public ApiResponse<List<ResPatchItemDto>> increaseQuantity(@RequestBody List<ReqPatchItemDto> itemList
		, @CurrentUser UserContext user
	){
		return ApiResponse.of(SuccessCode.OK, itemService.increaseQuantity(itemList, user));
	}

	@DeleteMapping("/{itemId}")
	@RequiredRole({UserRole.MASTER, UserRole.HUB_MANAGER})
	public ApiResponse<ResDeleteItemDto> deleteItem(@PathVariable("itemId") UUID itemId
		, @CurrentUser UserContext user
	){
		return ApiResponse.of(SuccessCode.OK, itemService.deleteItem(itemId, user));
	}

	@PatchMapping("/{itemId}")
	@RequiredRole({UserRole.MASTER, UserRole.HUB_MANAGER, UserRole.SUPPLIER_MANAGER})
	public ApiResponse<ResUpdateItemInfoDto> updateItemInfo(@PathVariable("itemId") UUID itemId
		, @RequestBody @Valid ReqUpdateItemInfoDto requestDto
		, @CurrentUser UserContext user
	){
		return ApiResponse.of(SuccessCode.OK, itemService.updateItemInfo(itemId, requestDto, user));
	}

	@GetMapping
	@RequiredRole({UserRole.MASTER, UserRole.HUB_MANAGER, UserRole.SUPPLIER_MANAGER, UserRole.DELIVERY_MANAGER})
	public ApiResponse<PageResponse<ResSearchItem>> searchItems(@RequestParam(defaultValue = "1") int page
			,@RequestParam(defaultValue = "10") int size
			,@RequestParam(defaultValue = "", required = false) String keyword
			,@CurrentUser UserContext user
		){

		int requestSize = size;
		int finalSize = ALLOWED_SIZES.contains(requestSize) ? requestSize : DEFAULT_SIZE;
		int zeroBasedPage = Math.max(0, page - 1);

		Sort sort = Sort.by(
			Sort.Order.desc("createdAt"), // 첫 번째 정렬: createdAt DESC
			Sort.Order.asc("itemName")    // 두 번째 정렬: itemName ASC
		);

		Pageable finalPageable = PageRequest.of(
			zeroBasedPage,
			finalSize,
			sort
		);

		Page<ResSearchItem> resultPageList = itemService.searchItems(finalPageable, keyword, user);
		PageResponse<ResSearchItem> responseDto = PageResponse.from(resultPageList);

		return ApiResponse.of(SuccessCode.OK, responseDto);

	}
}
