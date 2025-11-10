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

		// role 검증
		// master 통과

		// company 검증
		ResGetCompanyDto company = companyServiceClient.getCompany(requestDto.getCompanyId());

		// hub 검증
		ResGetHubDto hub = hubServiceClient.getHub(requestDto.getHubId());

		if ( hub == null || company == null ) { throw new IllegalArgumentException("허브 혹은 업체가 삭제되었거나, 존재하지 않습니다."); }
		if (company.getHubId() != hub.getHubId()){ throw new IllegalArgumentException("담당 허브 및 담당 업체만 상품을 등록할 수 있습니다."); }

		// 허브 관리자 -> 담당 허브인지 확인
		// 업체 관리자 -> 본인 업체 상품 등록인지 확인
		// CurrentUser.getHubId or getCompanyId

		Item item = Item.builder()
			.hubId(hub.getHubId())
			.companyId(company.getCompanyId())
			.itemName(requestDto.getItemName())
			.price(requestDto.getPrice())
			.quantity(requestDto.getQuantity())
			.build();

		Item newItem = itemRepository.save(item);

		return new ResCreateItemDto(newItem.getItemId(),
									newItem.getItemName(),
									hub.getHubName(),
									company.getCompanyName(),
									newItem.getPrice(),
									newItem.getQuantity()
		);

	}
}
