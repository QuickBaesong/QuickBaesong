// package com.qb.itemservice;
//
// import static org.mockito.BDDMockito.*;
// import static org.springframework.http.MediaType.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.UUID;
//
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.context.annotation.Import;
// import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.springframework.test.web.servlet.MockMvc;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.qb.itemservice.application.service.ItemService;
// import com.qb.itemservice.dto.ReqCreateItemDto;
// import com.qb.itemservice.dto.ReqPatchItemDto;
// import com.qb.itemservice.dto.ResCreateItemDto;
// import com.qb.itemservice.dto.ResGetItemDto;
// import com.qb.itemservice.dto.ResPatchItemDto;
// import com.qb.itemservice.presentation.controller.ItemController;
//
//
// @WebMvcTest(ItemController.class)
// @Import(ItemController.class)
// @DisplayName("Item Controller Test")
// public class ItemControllerTest {
//
// 	@Autowired
// 	private MockMvc mockMvc;
//
// 	@Autowired
// 	private ObjectMapper objectMapper;
//
// 	@MockitoBean
// 	private ItemService itemService;
//
// 	@MockitoBean
// 	private JpaMetamodelMappingContext jpaMappingContext;
//
// 	private UUID hubId =  UUID.randomUUID();
// 	private UUID companyId =  UUID.randomUUID();
//
// 	@Test
// 	@DisplayName("Item: 상품 등록 요청/응답 성공 테스트")
// 	void itemCreate_SuccessTest() throws Exception {
//
// 		// given
// 		ReqCreateItemDto reqCreateItemDto = new ReqCreateItemDto("짜파게티5입",hubId,companyId,5500L,10L);
// 		ResCreateItemDto resCreateItemDto = ResCreateItemDto.builder().
// 			itemId(UUID.randomUUID())
// 			.itemName("짜파게티5입")
// 			.hubName("경기허브")
// 			.companyName("농심")
// 			.price(5500L)
// 			.quantity(10L)
// 			.build();
//
//
// 		given(itemService.createItem(any(ReqCreateItemDto.class))).willReturn(resCreateItemDto);
//
// 		// when&then
// 		mockMvc.perform(post("/v1/items")
// 			.contentType(APPLICATION_JSON)
// 				.accept(APPLICATION_JSON)
// 			.content(objectMapper.writeValueAsString(reqCreateItemDto)))
// 			.andExpect(status().isOk())
// 			.andExpect(jsonPath("$.data.itemName").value("짜파게티5입"))
// 			.andExpect(jsonPath("$.data.hubName").value("경기허브"))
// 			.andExpect(jsonPath("$.data.companyName").value("농심"))
// 			.andExpect(jsonPath("$.data.price").value(5500))
// 			.andExpect(jsonPath("$.data.quantity").value(10));
//
// 	}
//
// 	@Test
// 	@DisplayName("Item: 단건조회 요청/응답 성공 테스트")
// 	void getItemByItemId_SuccessTest() throws Exception {
//
// 		// given
// 		UUID itemId = UUID.randomUUID();
//
// 		ResGetItemDto resGetItemDto = ResGetItemDto.builder()
// 			.itemId(itemId)
// 			.itemName("테스트상품")
// 			.companyId(UUID.randomUUID())
// 			.hubId(UUID.randomUUID())
// 			.price(5500L)
// 			.quantity(10L)
// 			.createdAt(LocalDateTime.now())
// 			.updatedAt(LocalDateTime.now())
// 			.build();
//
// 		when(itemService.getItem(itemId)).thenReturn(resGetItemDto);
//
// 		// when & then
//
// 		mockMvc.perform(get("/v1/items/{itemId}", itemId))
// 			.andExpect(status().isOk())
// 			.andExpect(jsonPath("$.data.price").value(5500L))
// 			.andExpect(jsonPath("$.data.quantity").value(10L));
// 	}
//
// 	@Test
// 	@DisplayName("Item : 재고 감소 요청/응답 성공 테스트")
// 	void decreaseQuantity_SuccessTest() throws Exception {
//
// 		// given
// 		UUID itemId1 = UUID.randomUUID();
// 		UUID itemId2 = UUID.randomUUID();
//
// 		// 서비스가 반환할 DTO 목록
// 		List<ResPatchItemDto> resList = List.of(
// 			new ResPatchItemDto(itemId1, 90L), // 감소 후 수량
// 			new ResPatchItemDto(itemId2, 0L)   // 감소 후 수량
// 		);
//
// 		// 서비스 Mocking
// 		given(itemService.decreaseQuantity(any())).willReturn(resList);
//
// 		// 요청 DTO
// 		List<ReqPatchItemDto> reqList = List.of(
// 			new ReqPatchItemDto(itemId1, 10L),
// 			new ReqPatchItemDto(itemId2, 200L)
// 		);
//
// 		// when & then
// 		mockMvc.perform(patch("/v1/items/decrease")
// 				.contentType(MediaType.APPLICATION_JSON)
// 				.content(objectMapper.writeValueAsString(reqList)))
// 			.andExpect(status().isOk())
// 			.andExpect(jsonPath("$.data").isArray())
// 			.andExpect(jsonPath("$.data[0].quantity").value(90))
// 			.andExpect(jsonPath("$.data[1].quantity").value(0));
// 	}
//
//
// }
