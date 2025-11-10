package com.qb.itemservice.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qb.itemservice.client.CompanyServiceClient;
import com.qb.itemservice.client.HubServiceClient;
import com.qb.itemservice.client.dto.ResGetCompanyDto;
import com.qb.itemservice.client.dto.ResGetHubDto;
import com.qb.itemservice.domain.entity.Item;
import com.qb.itemservice.domain.repository.ItemRepository;
import com.qb.itemservice.domain.service.CompanyHubPolicy;
import com.qb.itemservice.dto.ReqCreateItemDto;
import com.qb.itemservice.dto.ResCreateItemDto;
import com.qb.itemservice.dto.ResGetItemDto;

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
		ResGetCompanyDto company = companyServiceClient.getCompany(requestDto.getCompanyId());

		// hub 조회
		ResGetHubDto hub = hubServiceClient.getHub(requestDto.getHubId());

		companyHubPolicy.validateCompanyBelongsToHub(company.getHubId(), hub.getHubId());

		Item item = requestDto.toEntity();

		itemRepository.save(item);

		return ResCreateItemDto.fromEntity(item, hub.getHubName(), company.getCompanyName());

	}

	@Transactional(readOnly = true)
	public ResGetItemDto getItem(UUID itemId) {

		// user role

		Item item = itemRepository.findByItemIdAndDeletedAtIsNull(itemId).orElseThrow(()->{
			throw new IllegalArgumentException("존재하지 않는 상품입니다");
		});

		return ResGetItemDto.fromEntity(item);
	}
}
