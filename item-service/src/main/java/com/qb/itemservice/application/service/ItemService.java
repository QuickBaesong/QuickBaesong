package com.qb.itemservice.application.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.qb.common.annotations.CurrentUser;
import com.qb.common.enums.UserRole;
import com.qb.common.response.ApiResponse;
import com.qb.common.security.UserContext;
import com.qb.itemservice.client.CompanyServiceClient;
import com.qb.itemservice.client.HubServiceClient;
import com.qb.itemservice.client.UserServiceClient;
import com.qb.itemservice.client.dto.ResGetCompanyDto;
import com.qb.itemservice.client.dto.ResGetHubDto;
import com.qb.itemservice.client.dto.ResGetUserDto;
import com.qb.itemservice.domain.entity.Item;
import com.qb.itemservice.domain.repository.ItemRepository;
import com.qb.itemservice.domain.service.CompanyHubPolicy;
import com.qb.itemservice.dto.ReqCreateItemDto;
import com.qb.itemservice.dto.ReqPatchItemDto;
import com.qb.itemservice.dto.ReqUpdateItemInfoDto;
import com.qb.itemservice.dto.ResCreateItemDto;
import com.qb.itemservice.dto.ResDeleteItemDto;
import com.qb.itemservice.dto.ResGetItemDto;
import com.qb.itemservice.dto.ResPatchItemDto;
import com.qb.itemservice.dto.ResSearchItem;
import com.qb.itemservice.dto.ResUpdateItemInfoDto;
import com.qb.itemservice.exception.ItemCustomException;
import com.qb.itemservice.exception.ItemErrorCode;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;

	private final HubServiceClient hubServiceClient;

	private final CompanyServiceClient companyServiceClient;

	private final UserServiceClient userServiceClient;

	private final ValidateService validateService;

	@Transactional
	public ResCreateItemDto createItem(ReqCreateItemDto requestDto, @CurrentUser UserContext user) {

		// User 조회
		ResGetUserDto userDto = getUser(user);

		// Company 조회
		ApiResponse<ResGetCompanyDto> companyResponse = companyServiceClient.getCompany(requestDto.getCompanyId());
		ResGetCompanyDto companyDto = companyResponse.getData();

		// Hub 조회
		ApiResponse<ResGetHubDto> hubResponse = hubServiceClient.getHub(requestDto.getHubId());
		ResGetHubDto hubDto =  hubResponse.getData();

		validateService.validateHubCompany(userDto, companyDto, hubDto, user.role());

		Item item = requestDto.toEntity();

		itemRepository.save(item);

		return ResCreateItemDto.fromEntity(item, hubDto.getHubName(), companyDto.getCompanyName());

	}

	@Transactional(readOnly = true)
	public ResGetItemDto getItem(UUID itemId, UserContext user) {

		// User 조회
		ResGetUserDto userDto = getUser(user);

		Item item = itemRepository.findByItemIdAndDeletedAtIsNull(itemId).orElseThrow(()->{
			throw new ItemCustomException(ItemErrorCode.NOT_FOUND_ITEM);
		});

		validateService.validateItemHubCompany(item, userDto.getCompanyId(), user.role());

		return ResGetItemDto.fromEntity(item);
	}

	@Transactional
	public List<ResPatchItemDto> decreaseQuantity(List<ReqPatchItemDto> itemList
		, UserContext user
	) {

		List<UUID> itemsIds = itemList.stream()
			.map(ReqPatchItemDto::getItemId)
			.toList();

		List<Item> items = itemRepository.findAllByItemIdInAndDeletedAtIsNull(itemsIds);

		if (items.size() != itemsIds.size()) {
			throw new ItemCustomException(ItemErrorCode.NOT_FOUND_ITEM);
		}

		Map<UUID, Item> itemMap = items.stream()
			.collect(Collectors.toMap(Item::getItemId, Function.identity()));

		for(ReqPatchItemDto dto : itemList){
			Item item = itemMap.get(dto.getItemId());
			item.decreaseStock(dto.getQuantity());
		}

		return items.stream()
			.map(ResPatchItemDto::fromEntity)
			.toList();
	}

	@Transactional
	public List<ResPatchItemDto> increaseQuantity(List<ReqPatchItemDto> itemList
		, UserContext user
	) {

		List<UUID> itemsIds = itemList.stream()
			.map(ReqPatchItemDto::getItemId)
			.toList();

		List<Item> items = itemRepository.findAllByItemIdInAndDeletedAtIsNull(itemsIds);

		if (items.size() != itemsIds.size()) {
			throw new ItemCustomException(ItemErrorCode.NOT_FOUND_ITEM);
		}

		Map<UUID, Item> itemMap = items.stream()
			.collect(Collectors.toMap(Item::getItemId, Function.identity()));

		for(ReqPatchItemDto dto : itemList){
			Item item = itemMap.get(dto.getItemId());
			item.increaseStock(dto.getQuantity());
		}

		return items.stream()
			.map(ResPatchItemDto::fromEntity)
			.toList();
	}

	@Transactional
	public ResDeleteItemDto deleteItem(UUID itemId, UserContext user) {

		Item item = itemRepository.findByItemIdAndDeletedAtIsNull(itemId).orElseThrow(()-> new ItemCustomException(ItemErrorCode.NOT_FOUND_ITEM));

		// User 조회
		ResGetUserDto userDto = getUser(user);

		validateService.validateItemHubCompany(item, userDto.getCompanyId(), user.role());

		item.softDelete(user.username());

		return ResDeleteItemDto.fromEntity(item);

	}

	@Transactional
	public ResUpdateItemInfoDto updateItemInfo(UUID itemId, ReqUpdateItemInfoDto requestDto
		, UserContext user
	) {
		Item item = itemRepository.findByItemIdAndDeletedAtIsNull(itemId)
			.orElseThrow(()-> new ItemCustomException(ItemErrorCode.NOT_FOUND_ITEM));

		// User 조회
		ResGetUserDto userDto = getUser(user);

		validateService.validateItemHubCompany(item, userDto.getCompanyId(), user.role());

		item.updateInfo(requestDto.getPrice(), requestDto.getItemName());

		return ResUpdateItemInfoDto.toDto(item);
	}

	@Transactional(readOnly = true)
	public Page<ResSearchItem> searchItems(Pageable pageable, String keyword, UserContext user) {

		if (user.role() == UserRole.HUB_MANAGER){
			Page<Item> itemPages = this.searchForHubManager(pageable, keyword, user);
			return itemPages.map(ResSearchItem::fromEntity);
		}

		Page<Item> itemPages = this.searchForOthers(pageable, keyword);
		return itemPages.map(ResSearchItem::fromEntity);
	}


	private ResGetUserDto getUser(UserContext user) {
		ApiResponse<ResGetUserDto> getUserDtoApiResponse = userServiceClient.getUser(user.username());
		ResGetUserDto userDto = getUserDtoApiResponse.getData();
		return userDto;
	}

	private Page<Item> searchForHubManager(Pageable pageable, String keyword, UserContext user) {

		ResGetUserDto userDto = getUser(user);

		if (StringUtils.hasText(keyword)) {
			return itemRepository.findByHubIdNonDeletedByKeyword(pageable, userDto.getCompanyId(), keyword);
		}

		return itemRepository.findByHubIdNonDeleted(pageable, userDto.getCompanyId());
	}

	private Page<Item> searchForOthers(Pageable pageable, String keyword){
		if (StringUtils.hasText(keyword)) {
			return itemRepository.findAllNonDeletedByKeyword(pageable, keyword);
		}
		return itemRepository.findAllNonDeleted(pageable);
	}

}
