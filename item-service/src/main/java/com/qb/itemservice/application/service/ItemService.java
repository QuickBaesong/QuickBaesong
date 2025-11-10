package com.qb.itemservice.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qb.itemservice.client.CompanyServiceClient;
import com.qb.itemservice.client.HubServiceClient;
import com.qb.itemservice.client.dto.ResGetCompanyDto;
import com.qb.itemservice.client.dto.ResGetHubDto;
import com.qb.itemservice.domain.entity.Item;
import com.qb.itemservice.domain.repository.ItemRepository;
import com.qb.itemservice.dto.ReqCreateItemDto;
import com.qb.itemservice.dto.ResCreateItemDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;

	private final HubServiceClient hubServiceClient;

	private final CompanyServiceClient companyServiceClient;

	@Transactional
	public ResCreateItemDto createItem(ReqCreateItemDto requestDto) {

		// company 검증
		ResGetCompanyDto company = companyServiceClient.getCompany(requestDto.getCompanyId());

		// hub 검증
		ResGetHubDto hub = hubServiceClient.getHub(requestDto.getHubId());

		Item item = requestDto.toEntity();

		itemRepository.save(item);

		return ResCreateItemDto.fromEntity(item, hub.getHubName(), company.getCompanyName());

	}
}
