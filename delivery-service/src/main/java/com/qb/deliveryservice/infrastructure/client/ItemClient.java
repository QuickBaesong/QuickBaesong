package com.qb.deliveryservice.infrastructure.client;

import com.qb.common.response.ApiResponse;
import com.qb.deliveryservice.infrastructure.client.dto.ItemResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "item-service")
public interface ItemClient {

    @GetMapping("/v1/items/{itemId}")
    ApiResponse<ItemResponse> getItem(@PathVariable("itemId") UUID itemId);
}