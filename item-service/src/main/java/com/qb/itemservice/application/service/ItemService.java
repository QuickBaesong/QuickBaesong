package com.qb.itemservice.application.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qb.common.response.ApiResponse;
import com.qb.itemservice.client.CompanyServiceClient;
import com.qb.itemservice.client.HubServiceClient;
import com.qb.itemservice.client.dto.ResGetCompanyDto;
import com.qb.itemservice.client.dto.ResGetHubDto;
import com.qb.itemservice.domain.entity.Item;
import com.qb.itemservice.domain.repository.ItemRepository;
import com.qb.itemservice.domain.service.CompanyHubPolicy;
import com.qb.itemservice.dto.ReqCreateItemDto;
import com.qb.itemservice.dto.ReqPatchItemDto;
import com.qb.itemservice.dto.ResCreateItemDto;
import com.qb.itemservice.dto.ResDeleteItemDto;
import com.qb.itemservice.dto.ResGetItemDto;
import com.qb.itemservice.dto.ResPatchItemDto;
import com.qb.itemservice.exception.ItemCustomException;
import com.qb.itemservice.exception.ItemErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;

	private final HubServiceClient hubServiceClient;

	private final CompanyServiceClient companyServiceClient;

	private final CompanyHubPolicy companyHubPolicy;

	@Transactional
	public ResCreateItemDto createItem(ReqCreateItemDto requestDto) {

		// company 조회
		ApiResponse<ResGetCompanyDto> companyResponse = companyServiceClient.getCompany(requestDto.getCompanyId());
		ResGetCompanyDto company = companyResponse.getData();

		// hub 조회
		ApiResponse<ResGetHubDto> hubResponse = hubServiceClient.getHub(requestDto.getHubId());
		ResGetHubDto hub =  hubResponse.getData();

		companyHubPolicy.validateCompanyBelongsToHub(company.getHubId(), hub.getHubId());

		Item item = requestDto.toEntity();

		itemRepository.save(item);

		return ResCreateItemDto.fromEntity(item, hub.getHubName(), company.getCompanyName());

	}

	@Transactional(readOnly = true)
	public ResGetItemDto getItem(UUID itemId) {

		// user role

		Item item = itemRepository.findByItemIdAndDeletedAtIsNull(itemId).orElseThrow(()->{
			throw new ItemCustomException(ItemErrorCode.NOT_FOUND_ITEM);
		});

		return ResGetItemDto.fromEntity(item);
	}

	@Transactional
	public List<ResPatchItemDto> decreaseQuantity(List<ReqPatchItemDto> itemList) {

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
	public List<ResPatchItemDto> increaseQuantity(List<ReqPatchItemDto> itemList) {

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
	public ResDeleteItemDto deleteItem(UUID itemId) {

		Item item = itemRepository.findByItemIdAndDeletedAtIsNull(itemId).orElseThrow(()-> new ItemCustomException(ItemErrorCode.NOT_FOUND_ITEM));

		// user 소속 업체/허브와 아이템 허브/아이디 비교

		item.softDelete("");

		return ResDeleteItemDto.fromEntity(item);

	}
}
