package com.qb.itemservice;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qb.itemservice.application.service.ItemService;
import com.qb.itemservice.dto.ReqCreateItemDto;
import com.qb.itemservice.dto.ResCreateItemDto;
import com.qb.itemservice.presentation.controller.ItemController;

import lombok.extern.slf4j.Slf4j;

@WebMvcTest(ItemController.class)
@Import(ItemController.class)
@Slf4j
@DisplayName("Item Controller Test")
public class ItemControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private ItemService itemService;

	@MockitoBean
	private JpaMetamodelMappingContext jpaMappingContext;

	private UUID hubId =  UUID.randomUUID();
	private UUID companyId =  UUID.randomUUID();

	@Test
	@DisplayName("정상적인 상품 등록 요청시 201 응답과 DTO를 반환")
	void itemCreate_SuccessTest() throws Exception {

		// given
		ReqCreateItemDto reqCreateItemDto = new ReqCreateItemDto("짜파게티5입",hubId,companyId,5500L,10L);
		ResCreateItemDto resCreateItemDto = ResCreateItemDto.builder().
			itemId(UUID.randomUUID())
			.itemName("짜파게티5입")
			.hubName("경기허브")
			.companyName("농심")
			.price(5500L)
			.quantity(10L)
			.build();


		given(itemService.createItem(any(ReqCreateItemDto.class))).willReturn(resCreateItemDto);

		// when&then
		mockMvc.perform(post("/v1/items")
			.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(reqCreateItemDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.itemName").value("짜파게티5입"))
			.andExpect(jsonPath("$.data.hubName").value("경기허브"))
			.andExpect(jsonPath("$.data.companyName").value("농심"))
			.andExpect(jsonPath("$.data.price").value(5500))
			.andExpect(jsonPath("$.data.quantity").value(10));

	}



}
