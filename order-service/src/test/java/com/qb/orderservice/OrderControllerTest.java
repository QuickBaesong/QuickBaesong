package com.qb.orderservice;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qb.orderservice.application.service.OrderService;
import com.qb.orderservice.domain.entity.OrderItem;
import com.qb.orderservice.dto.ReqCreateOrderDto;
import com.qb.orderservice.dto.ResCreateOrderDto;
import com.qb.orderservice.presentation.controller.OrderController;


@WebMvcTest
@Import(OrderController.class)
@DisplayName("Order Controller Test")
public class OrderControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private OrderService orderService;

	private UUID hubId = UUID.randomUUID();
	private UUID orderId = UUID.randomUUID();
	private UUID deliveryId = UUID.randomUUID();

	private UUID sender =  UUID.randomUUID();
	private UUID receiver = UUID.randomUUID();

	private UUID userId = UUID.randomUUID();

	private UUID testItemOne = UUID.randomUUID();
	private Long testItemOnePrice = 500L;
	private Long testItemOneQuantity = 100L;

	private UUID testItemTwo =UUID.randomUUID();
	private Long testItemTwoPrice = 660L;
	private Long testItemTwoQuantity = 200L;

	private LocalDateTime requiredDeliveryAt = LocalDateTime.now();
	private String testAddress = "testAddress";
	private String testSlackId = "testSlackId";
	private String recipientName = "테스터";

	@Test
	@DisplayName("정상적인 주문 생성 요청시 200 응답과 함께 DTO 반환")
	void orderCreate_SuccessTest() throws Exception {
		// given
		List<ReqCreateOrderDto.OrderItemDto> orderItems = new ArrayList<>();

		ReqCreateOrderDto.OrderItemDto orderItemDtoOne = new ReqCreateOrderDto.OrderItemDto(
			testItemOne,
			testItemOneQuantity,
			testItemOnePrice
		);

		ReqCreateOrderDto.OrderItemDto orderItemDtoTwo = new ReqCreateOrderDto.OrderItemDto(
			testItemTwo,
			testItemTwoQuantity,
			testItemOnePrice
		);

		orderItems.add(orderItemDtoOne);
		orderItems.add(orderItemDtoTwo);

		ReqCreateOrderDto reqCreateOrderDto = new ReqCreateOrderDto(
			hubId,
			sender,
			receiver,
			userId,
			requiredDeliveryAt,
			orderItems,
			testAddress,
			testSlackId,
			recipientName
		);

		List<OrderItem> orderItemList = new ArrayList<>();

		OrderItem orderItemOne = new OrderItem(testItemOne, testItemOneQuantity, testItemOnePrice);
		orderItemList.add(orderItemOne);
		OrderItem orderItemTwo = new OrderItem(testItemTwo, testItemTwoQuantity,testItemTwoPrice);
		orderItemList.add(orderItemTwo);

		ResCreateOrderDto resCreateOrderDto = new ResCreateOrderDto(
			orderId,
			hubId,
			sender,
			receiver,
			orderItemList,
			Long.toString(testItemOnePrice * testItemOneQuantity)+(testItemTwoPrice+testItemTwoQuantity),
			Long.toString(testItemOneQuantity + testItemTwoQuantity),
			testAddress,
			testSlackId,
			recipientName,
			requiredDeliveryAt
		);

		given(orderService.createOrder(any(ReqCreateOrderDto.class))).willReturn(resCreateOrderDto);

		// when & then
		mockMvc.perform(post("/v1/orders")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(reqCreateOrderDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.orderId").value(orderId.toString()))
			.andExpect(jsonPath("$.data.hubId").value(hubId.toString()))
			.andExpect(jsonPath("$.data.orderItems").isArray())
			.andExpect(jsonPath("$.data.orderItems").isNotEmpty());

	}

}
