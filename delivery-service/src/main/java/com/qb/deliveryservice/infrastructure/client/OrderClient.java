package com.qb.deliveryservice.infrastructure.client;

import com.qb.common.response.ApiResponse;
import com.qb.deliveryservice.infrastructure.client.dto.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "order-service")
public interface OrderClient {

    @GetMapping("/v1/orders/{orderId}")
    ApiResponse<OrderResponse> getOrder(@PathVariable("orderId") UUID orderId);
}
