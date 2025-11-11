package com.qb.itemservice;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.qb.itemservice.application.service.ItemService;
import com.qb.itemservice.client.CompanyServiceClient;
import com.qb.itemservice.client.HubServiceClient;
import com.qb.itemservice.client.dto.ResGetCompanyDto;
import com.qb.itemservice.client.dto.ResGetHubDto;
import com.qb.itemservice.domain.entity.Item;
import com.qb.itemservice.domain.repository.ItemRepository;
import com.qb.itemservice.dto.ReqCreateItemDto;
import com.qb.itemservice.dto.ResCreateItemDto;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

	@Mock
	private ItemRepository itemRepository;

	@Mock
	private HubServiceClient hubServiceClient;

	@Mock
	private CompanyServiceClient companyServiceClient;

	@InjectMocks
	private ItemService itemService;

	private String userName = "username";
	private UUID hubId = UUID.randomUUID();
	private UUID companyId = UUID.randomUUID();

	// @Test
	// @DisplayName("허브와 업체가 모두 존재할 때, 제품 생성 성공")
	// void createItem_successTest() throws NoSuchFieldException, IllegalAccessException {
	//
	// 	// given
	//
	// 	ReqCreateItemDto reqCreateItemDto = new ReqCreateItemDto("testItem",hubId,companyId,100L,5L);
	// 	ResGetHubDto hub = new ResGetHubDto(hubId,"경기센터");
	// 	ResGetCompanyDto company = new ResGetCompanyDto(companyId, "테스트업체", hubId, "SENDER");
	//
	// 	given(hubServiceClient.getHub(hubId)).willReturn(hub);
	// 	given(companyServiceClient.getCompany(companyId)).willReturn(company);
	//
	// 	Item savedItem = Item.builder()
	// 		.companyId(companyId)
	// 		.hubId(hubId)
	// 		.itemName("testItem")
	// 		.price(100L)
	// 		.quantity(5L)
	// 		.build();
	//
	// 	Field itemIdField = Item.class.getDeclaredField("itemId");
	// 	itemIdField.setAccessible(true);
	// 	itemIdField.set(savedItem, UUID.randomUUID());
	//
	// 	given(itemRepository.save(any(Item.class))).willReturn(savedItem);
	//
	// 	// when
	// 	ResCreateItemDto result = itemService.createItem(reqCreateItemDto);
	//
	// 	// then
	//
	// 	assertThat(result).isNotNull();
	// 	assertThat(result.getItemId()).isNotNull();
	// 	assertThat(result.getItemName()).isEqualTo("testItem");
	// 	assertThat(result.getPrice()).isEqualTo(100L);
	// 	assertThat(result.getQuantity()).isEqualTo(5L);
	//
	// }

}
