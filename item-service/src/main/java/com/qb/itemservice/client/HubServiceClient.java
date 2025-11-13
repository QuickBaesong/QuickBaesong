package com.qb.itemservice.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.qb.common.interceptor.FeignClientInterceptor;
import com.qb.common.response.ApiResponse;
import com.qb.itemservice.client.dto.ResGetHubDto;

@FeignClient(name = "hub-service", configuration = FeignClientInterceptor.class)
public interface HubServiceClient {

	@GetMapping(value = "/v1/hub/{hubId}")
	ApiResponse<ResGetHubDto> getHub(@PathVariable("hubId") UUID hubId);

}
